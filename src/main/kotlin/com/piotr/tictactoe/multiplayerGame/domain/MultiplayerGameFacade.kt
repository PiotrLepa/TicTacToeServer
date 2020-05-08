package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.core.firebase.FirebasePushService
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import com.piotr.tictactoe.multiplayerGame.converter.MultiplayerGameCreatedDtoConverterArgs
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.FIRST_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.SECOND_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameChecker
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameHelper
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import com.piotr.tictactoe.multiplayerGame.dto.PlayerType
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MultiplayerGameFacade @Autowired constructor(
  private val multiplayerGameRepository: MultiplayerGameRepository,
  private val multiplayerGameHelper: MultiplayerGameHelper,
  private val userFacade: UserFacade,
  private val gameMoveFacade: GameMoveFacade,
  private val multiplayerGameDispatcher: MultiplayerGameDispatcher,
  private val multiplayerGameChecker: MultiplayerGameChecker,
  private val firebasePushService: FirebasePushService,
  private val multiplayerGameDtoConverter: ConverterWithArgs<MultiplayerGame, MultiplayerGameDto, AllGameMovesDto>,
  private val multiplayerGameCreatedDtoConverter: ConverterWithArgs<MultiplayerGame, MultiplayerGameCreatedDto, MultiplayerGameCreatedDtoConverterArgs>,
  private val multiplayerGameResultDtoConverter: Converter<MultiplayerGame, MultiplayerGameResultDto>
) {

  fun createMultiplayerGame(opponentCode: String): MultiplayerGameCreatedDto {
    val firstPlayer = userFacade.getLoggedInUser()
    val secondPlayer = userFacade.findUserByPlayerCode(opponentCode) ?: throw InvalidOpponentCodeException()
    multiplayerGameChecker.checkIfPlayerInvitedHimself(firstPlayer, secondPlayer)

    val game = multiplayerGameHelper.createMultiplayerGame(firstPlayer, secondPlayer)
        .let(multiplayerGameRepository::save)

    val secondPlayerConverterArgs = MultiplayerGameCreatedDtoConverterArgs(
        yourMark = game.secondPlayerMark,
        playerType = PlayerType.SECOND_PLAYER
    )
    val secondPlayerGameDto = multiplayerGameCreatedDtoConverter.convert(game, secondPlayerConverterArgs)
    firebasePushService.sendGameInvitation(secondPlayer.deviceToken, secondPlayerGameDto, firstPlayer)

    val firstPlayerConverterArgs = MultiplayerGameCreatedDtoConverterArgs(
        yourMark = game.firstPlayerMark,
        playerType = PlayerType.FIRST_PLAYER
    )
    return multiplayerGameCreatedDtoConverter.convert(game, firstPlayerConverterArgs)
  }

  fun joinToGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedInUser()

    multiplayerGameChecker.checkIfOpponentIsCorrect(game, player)
    multiplayerGameChecker.checkIfGameHasNotStarted(game)

    val updatedGame = multiplayerGameRepository.save(game.copy(
        status = MultiplayerGameStatus.ON_GOING
    ))
    val gameDto = multiplayerGameDtoConverter.convert(updatedGame, AllGameMovesDto(listOf()))
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedInUser()

    multiplayerGameChecker.checkIfGameIsOnGoing(game)
    multiplayerGameChecker.checkIfPlayerMatch(game, player)
    multiplayerGameChecker.checkGameTurn(game, player)

    val moves = gameMoveFacade.setMultiplayerMove(gameId, fieldIndex, multiplayerGameHelper.getMarkForPlayer(game, player)).moves

    val updatedGame = multiplayerGameRepository.save(game.copy(
        currentTurn = if (game.currentTurn == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER,
        status = multiplayerGameHelper.getGameResult(moves, game.firstPlayerMark)
    ))
    val gameDto = multiplayerGameDtoConverter.convert(updatedGame, AllGameMovesDto(moves))
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun setPlayerLeftFromGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    if (game.status in MultiplayerGameStatus.getFinishedGameStatus()) {
      return
    }
    val allMovesDto = gameMoveFacade.getMultiplayerAllMoves(gameId)
    val updatedGame = multiplayerGameRepository.save(game.copy(
        status = MultiplayerGameStatus.PLAYER_LEFT_GAME
    ))
    val gameDto = multiplayerGameDtoConverter.convert(updatedGame, allMovesDto)
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun restartGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedInUser()
    val opponentId = if (game.firstPlayerId == player.id) game.secondPlayerId else game.firstPlayerId

    multiplayerGameChecker.checkIfGameFinished(game)

    val opponentRestartedGame = multiplayerGameRepository.findRecentGameByPlayerId(opponentId)

    val gameToSave = if (game.gameId == opponentRestartedGame.gameId) {
      game.copy(
          gameId = null,
          currentTurn = multiplayerGameHelper.getStartingPlayer(),
          status = MultiplayerGameStatus.CREATED
      )
    } else {
      game.copy(status = MultiplayerGameStatus.ON_GOING)
    }

    val newGame = multiplayerGameRepository.save(gameToSave)
    val gameDto = multiplayerGameDtoConverter.convert(newGame, AllGameMovesDto(listOf()))
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun getUserGames(pageable: Pageable, playerId: Long): Page<MultiplayerGameResultDto> =
      multiplayerGameRepository.findPlayerGameResultsOrderByModificationDateDesc(
          pageable,
          MultiplayerGameStatus.getFinishedGameStatus(),
          playerId
      ).map(multiplayerGameResultDtoConverter::convert)

  fun getAllGames(pageable: Pageable): Page<MultiplayerGameResultDto> =
      multiplayerGameRepository.findAllByStatusInOrderByModificationDateDesc(
          pageable,
          MultiplayerGameStatus.getFinishedGameStatus()
      ).map(multiplayerGameResultDtoConverter::convert)

  fun getGameDetails(gameId: Long): MultiplayerGameResultDto =
      multiplayerGameRepository.findGameByGameId(gameId).let(multiplayerGameResultDtoConverter::convert)
}