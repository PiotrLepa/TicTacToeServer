package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.FIRST_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.SECOND_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameHelper
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import com.piotr.tictactoe.multiplayerGame.exception.GameAlreadyStaredException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidPlayerException
import com.piotr.tictactoe.multiplayerGame.exception.OpponentMoveException
import com.piotr.tictactoe.singlePlayerGame.exception.GameEndedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.utils.GameEndChecker
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
  private lateinit var gameEndChecker: GameEndChecker

  @Autowired
  private lateinit var multiplayerGameDtoConverter: Converter1<MultiplayerGame, MultiplayerGameDto, AllGameMovesDto>

  @Autowired
  private lateinit var multiplayerGameCreatedDtoConverter: Converter1<MultiplayerGame, MultiplayerGameCreatedDto, FieldMark>

  fun createMultiplayerGame(opponentCode: String): MultiplayerGameCreatedDto {
    val firstPlayer = userFacade.getLoggedUser()
    val secondPlayer = userFacade.findUserByPlayerCode(opponentCode) ?: throw InvalidOpponentCodeException()
    val game = multiplayerGameHelper.createMultiplayerGame(firstPlayer, secondPlayer)
        .let(multiplayerGameRepository::save)

    // TODO send push to second player
    return multiplayerGameCreatedDtoConverter.convert(game, game.firstPlayerMark)
  }

  fun joinToGame(gameId: Long) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedUser()
    checkIfGameHasNotStarted(game)
    checkIfPlayerIsCorrect(game, player)
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

    checkIfGameIsOnGoing(game)
    checkIfPlayerMatch(game)
    checkGameTurn(game, player)

    gameMoveFacade.setMove(gameId, fieldIndex, getMarkForPlayer(game, player))
    val allMovesDto = gameMoveFacade.getAllMoves(gameId)

    val gameToSave = game.copy(
        currentTurn = if (game.currentTurn == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER,
        status = checkGameStatus(allMovesDto.moves, game.firstPlayerMark),
        modificationDate = DateTime.now().millis
    )
    val gameDto = multiplayerGameDtoConverter.convert(multiplayerGameRepository.save(gameToSave), allMovesDto)
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  private fun getMarkForPlayer(game: MultiplayerGame, player: UserDto): FieldMark {
    return if (game.firstPlayerId == player.id) {
      game.firstPlayerMark
    } else {
      game.secondPlayerMark
    }
  }

  private fun checkGameStatus(moves: List<GameMoveDto>, firstPlayer: FieldMark): MultiplayerGameStatus =
      when (gameEndChecker.checkGameEnd(moves, firstPlayer)) {
        GameEndChecker.GameResultType.WON -> MultiplayerGameStatus.FIRST_PLAYER_WON
        GameEndChecker.GameResultType.LOST -> MultiplayerGameStatus.SECOND_PLAYER_WON
        GameEndChecker.GameResultType.DRAW -> MultiplayerGameStatus.DRAW
        GameEndChecker.GameResultType.ON_GOING -> MultiplayerGameStatus.ON_GOING
      }

  private fun checkIfPlayerIsCorrect(game: MultiplayerGame, player: UserDto) {
    if (game.secondPlayerId != player.id) {
      throw InvalidPlayerException()
    }
  }

  private fun checkIfGameHasNotStarted(game: MultiplayerGame) {
    if (game.status != MultiplayerGameStatus.CREATED) {
      throw GameAlreadyStaredException()
    }
  }

  private fun checkGameTurn(game: MultiplayerGame, player: UserDto) {
    val correctPlayer = when (game.currentTurn) {
      FIRST_PLAYER -> game.firstPlayerId == player.id
      SECOND_PLAYER -> game.secondPlayerId == player.id
    }
    if (!correctPlayer) {
      throw OpponentMoveException()
    }
  }

  private fun checkIfPlayerMatch(game: MultiplayerGame) {
    val player = userFacade.getLoggedUser()
    if (player.id != game.firstPlayerId && player.id != game.secondPlayerId) {
      throw WrongPlayerException()
    }
  }

  private fun checkIfGameIsOnGoing(game: MultiplayerGame) {
    if (game.status != MultiplayerGameStatus.ON_GOING) {
      throw GameEndedException()
    }
  }
}