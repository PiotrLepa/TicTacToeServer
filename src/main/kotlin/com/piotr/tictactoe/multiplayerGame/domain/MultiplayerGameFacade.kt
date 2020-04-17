package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.converter.MultiplayerGameDtoConverterParams
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.FIRST_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn.SECOND_PLAYER
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameHelper
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
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
  private lateinit var multiplayerGameDtoConverter: Converter1<MultiplayerGame, MultiplayerGameDto, MultiplayerGameDtoConverterParams>

  @Autowired
  private lateinit var multiplayerGameCreatedDtoConverter: Converter<MultiplayerGame, MultiplayerGameCreatedDto>

  fun createMultiplayerGame(opponentCode: String): MultiplayerGameCreatedDto {
    val firstPlayer = userFacade.getLoggedUser()
    val secondPlayer = userFacade.findUserByPlayerCode(opponentCode) ?: throw InvalidOpponentCodeException()

    val game = multiplayerGameHelper.createMultiplayerGame(firstPlayer, secondPlayer)
        .let(multiplayerGameRepository::save)

    val gameDto = mapGameDto(firstPlayer, secondPlayer, game, listOf())
    multiplayerGameDispatcher.initGame(gameDto)

    return multiplayerGameCreatedDtoConverter.convert(game)
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int) {
    val game = multiplayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedUser()
    checkIfGameIsOnGoing(game)
    checkIfPlayerMatch(game)
    checkGameTurn(game, player)
    gameMoveFacade.setMove(gameId, fieldIndex, getMarkForPlayer(game, player))
    val allMovesDto = gameMoveFacade.getAllMoves(gameId)
    val (firstPlayer, secondPlayer) = if (game.firstPlayerId == player.id) {
      player to userFacade.findUserById(game.secondPlayerId)
    } else {
      userFacade.findUserById(game.firstPlayerId) to player
    }
    val gameToSave = game.copy(
        currentTurn = if (game.currentTurn == FIRST_PLAYER) SECOND_PLAYER else FIRST_PLAYER,
        status = MultiplayerGameStatus.ON_GOING, // TODO check game end
        modificationDate = DateTime.now().millis
    )
    val gameDto = mapGameDto(firstPlayer, secondPlayer, multiplayerGameRepository.save(gameToSave), allMovesDto.moves)
    multiplayerGameDispatcher.updateGameStatus(gameDto)
  }

  private fun mapGameDto(
    firstPlayer: UserDto,
    secondPlayer: UserDto,
    game: MultiplayerGame,
    moves: List<GameMoveDto>
  ): MultiplayerGameDto {
    val converterParams = MultiplayerGameDtoConverterParams(
        firstPlayerCode = firstPlayer.playerCode,
        secondPlayerCode = secondPlayer.playerCode,
        moves = moves
    )
    return multiplayerGameDtoConverter.convert(game, converterParams)
  }

  private fun getMarkForPlayer(game: MultiplayerGame, player: UserDto): FieldMark {
    return if (game.firstPlayerId == player.id) {
      game.firstPlayerMark
    } else {
      game.secondPlayerMark
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