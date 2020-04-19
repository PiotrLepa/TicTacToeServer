package com.piotr.tictactoe.singlePlayerGame.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameTurn
import com.piotr.tictactoe.singlePlayerGame.domain.utils.SinglePlayerGameHelper
import com.piotr.tictactoe.singlePlayerGame.domain.utils.computerMoveLogic.ComputerMoveLogic
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import com.piotr.tictactoe.singlePlayerGame.exception.GameEndedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.utils.GameEndChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SinglePlayerGameFacade @Autowired constructor(
  private val singlePlayerGameRepository: SinglePlayerGameRepository,
  private val userFacade: UserFacade,
  private val gameMoveFacade: GameMoveFacade,
  private val singlePlayerGameHelper: SinglePlayerGameHelper,
  private val computerMoveLogic: ComputerMoveLogic,
  private val gameEndChecker: GameEndChecker,
  private val singlePlayerDtoMapperGame: Converter1<SinglePlayerGame, SinglePlayerGameDto, AllGameMovesDto>,
  private val singlePlayerDetailsDtoMapper: Converter<SinglePlayerGame, SinglePlayerGameDetailsDto>
) {

  fun createSinglePlayerGame(difficultyLevel: DifficultyLevel): SinglePlayerGameDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = singlePlayerGameHelper.getStartingPlayer()
    val game = singlePlayerGameHelper.createSinglePlayerGame(player.id, difficultyLevel, startingPlayer)
        .let(singlePlayerGameRepository::save)
    val moves = if (startingPlayer == SinglePlayerGameTurn.COMPUTER) {
      val computerMove = setComputeMove(game.gameId!!, game.difficultyLevel, game.computerMark, listOf())
      listOf(computerMove)
    } else {
      listOf()
    }
    return updateGame(game, moves, game.playerMark)
  }

  fun setPlayerMoveAndGetComputerMove(gameId: Long, fieldIndex: Int): SinglePlayerGameDto {
    val game = singlePlayerGameRepository.findGameByGameId(gameId)
    checkIfGameIsOnGoing(game)
    checkIfPlayerMatch(game)
    gameMoveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val allMovesDto = gameMoveFacade.getAllMoves(gameId)
    val moves = allMovesDto.moves
    val updatedMoves = if (canDoNextMove(moves, game.playerMark)) {
      val computerMove = setComputeMove(gameId, game.difficultyLevel, game.computerMark, moves)
      moves + listOf(computerMove)
    } else {
      moves
    }
    return updateGame(game, updatedMoves, game.playerMark)
  }

  fun getUserGames(pageable: Pageable, playerId: Long): Page<SinglePlayerGameDetailsDto> =
      singlePlayerGameRepository.findAllByStatusInAndPlayerIdOrderByModificationDateDesc(
          pageable,
          SinglePlayerGameStatus.getEndedGameStatus(),
          playerId
      ).map(singlePlayerDetailsDtoMapper::convert)

  fun getAllGames(pageable: Pageable): Page<SinglePlayerGameDetailsDto> =
      singlePlayerGameRepository.findAllByStatusInOrderByModificationDateDesc(
          pageable,
          SinglePlayerGameStatus.getEndedGameStatus()
      ).map(singlePlayerDetailsDtoMapper::convert)

  fun getGameDetails(gameId: Long): SinglePlayerGameDetailsDto =
      singlePlayerGameRepository.findGameByGameId(gameId).let(singlePlayerDetailsDtoMapper::convert)

  private fun updateGame(
    game: SinglePlayerGame,
    moves: List<GameMoveDto>,
    playerMark: FieldMark
  ): SinglePlayerGameDto {
    val gameToSave = game.copy(
        status = checkGameStatus(moves, playerMark)
    )
    val savedGame = singlePlayerGameRepository.save(gameToSave)
    return singlePlayerDtoMapperGame.convert(savedGame, AllGameMovesDto(moves = moves))
  }

  private fun setComputeMove(
    gameId: Long,
    difficultyLevel: DifficultyLevel,
    computerMark: FieldMark,
    moves: List<GameMoveDto>
  ): GameMoveDto {
    val computerMoveFieldIndex = computerMoveLogic.calculateComputerMove(difficultyLevel, computerMark, moves)
    return gameMoveFacade.setMove(gameId, computerMoveFieldIndex, computerMark)
  }

  private fun checkGameStatus(
    moves: List<GameMoveDto>,
    playerMark: FieldMark
  ): SinglePlayerGameStatus = when (gameEndChecker.checkGameEnd(moves, playerMark)) {
    GameEndChecker.GameResultType.WON -> SinglePlayerGameStatus.PLAYER_WON
    GameEndChecker.GameResultType.LOST -> SinglePlayerGameStatus.COMPUTER_WON
    GameEndChecker.GameResultType.DRAW -> SinglePlayerGameStatus.DRAW
    GameEndChecker.GameResultType.ON_GOING -> SinglePlayerGameStatus.ON_GOING
  }

  private fun canDoNextMove(moves: List<GameMoveDto>, playerMark: FieldMark) =
      gameEndChecker.checkGameEnd(moves, playerMark) == GameEndChecker.GameResultType.ON_GOING

  private fun checkIfPlayerMatch(game: SinglePlayerGame) {
    val player = userFacade.getLoggedUser()
    if (player.id != game.playerId) {
      throw WrongPlayerException()
    }
  }

  private fun checkIfGameIsOnGoing(game: SinglePlayerGame) {
    if (game.status != SinglePlayerGameStatus.ON_GOING) {
      throw GameEndedException()
    }
  }
}