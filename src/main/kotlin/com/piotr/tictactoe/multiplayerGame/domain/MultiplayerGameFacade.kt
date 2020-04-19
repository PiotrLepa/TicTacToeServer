package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.core.converter.Converter2
import com.piotr.tictactoe.core.firebase.FirebasePushService
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.FIRST_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.SECOND_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameHelper
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import com.piotr.tictactoe.multiplayerGame.dto.PlayerType
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.user.domain.UserFacade
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MultiplayerGameFacade {

  @Autowired
  private lateinit var multiplayerGameRepository: MultiplayerGameRepository

  @Autowired
  private lateinit var multiplayerGameHelper: MultiplayerGameHelper

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var gameMoveFacade: GameMoveFacade

  @Autowired
  private lateinit var multiplayerGameDispatcher: MultiplayerGameDispatcher

  @Autowired
  private lateinit var multiplayerGameChecker: MultiplayerGameChecker

  @Autowired
  private lateinit var firebasePushService: FirebasePushService

  @Autowired
  private lateinit var multiplayerGameDtoConverter: Converter1<MultiplayerGame, MultiplayerGameDto, AllGameMovesDto>

  @Autowired
  private lateinit var multiplayerGameCreatedDtoConverter: Converter2<MultiplayerGame, MultiplayerGameCreatedDto, FieldMark, PlayerType>

  fun createMultiplayerGame(opponentCode: String): MultiplayerGameCreatedDto {
    val firstPlayer = userFacade.getLoggedUser()
    val secondPlayer = userFacade.findUserByPlayerCode(opponentCode) ?: throw InvalidOpponentCodeException()
    val game = multiplayerGameHelper.createMultiplayerGame(firstPlayer, secondPlayer)
        .let(multiplayerGameRepository::save)

    val secondPlayerGameDto = multiplayerGameCreatedDtoConverter.convert(game, game.secondPlayerMark, PlayerType.SECOND_PLAYER)
    firebasePushService.sendGameInvitation(secondPlayer.deviceToken, secondPlayerGameDto)

    return multiplayerGameCreatedDtoConverter.convert(game, game.firstPlayerMark, PlayerType.FIRST_PLAYER)
  }

  fun joinToGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedUser()

    multiplayerGameChecker.checkIfOpponentIsCorrect(game, player)
    multiplayerGameChecker.checkIfGameHasNotStarted(game)

    val updatedGame = multiplayerGameRepository.save(game.copy(
        status = MultiplayerGameStatus.ON_GOING,
        modificationDate = DateTime.now().millis
    ))
    val gameDto = multiplayerGameDtoConverter.convert(updatedGame, AllGameMovesDto(listOf()))
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedUser()

    multiplayerGameChecker.checkIfGameIsOnGoing(game)
    multiplayerGameChecker.checkIfPlayerMatch(game, player)
    multiplayerGameChecker.checkGameTurn(game, player)

    gameMoveFacade.setMove(gameId, fieldIndex, multiplayerGameHelper.getMarkForPlayer(game, player))
    val allMovesDto = gameMoveFacade.getAllMoves(gameId)

    val gameToSave = game.copy(
        currentTurn = if (game.currentTurn == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER,
        status = multiplayerGameHelper.getGameResult(allMovesDto.moves, game.firstPlayerMark),
        modificationDate = DateTime.now().millis
    )
    val gameDto = multiplayerGameDtoConverter.convert(multiplayerGameRepository.save(gameToSave), allMovesDto)
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  fun setPlayerLeftFromGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    if (game.status in MultiplayerGameStatus.getEndedGameStatus()) {
      return
    }
    multiplayerGameRepository.save(game.copy(
        status = MultiplayerGameStatus.PLAYER_LEFT_GAME,
        modificationDate = DateTime.now().millis
    ))
  }
}