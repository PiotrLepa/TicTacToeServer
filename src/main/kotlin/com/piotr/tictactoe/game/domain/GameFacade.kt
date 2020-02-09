package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameStatus.ON_GOING
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.domain.util.GameEndChecker
import com.piotr.tictactoe.game.domain.util.computermove.ComputerMoveGetter
import com.piotr.tictactoe.game.dto.GameResultDetailsDto
import com.piotr.tictactoe.game.dto.GameResultDto
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.MoveFacade
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import com.piotr.tictactoe.user.domain.UserFacade
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameFacade {

  @Autowired
  private lateinit var gameRepository: GameRepository

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var moveFacade: MoveFacade

  @Autowired
  private lateinit var gameComponent: GameComponent

  @Autowired
  private lateinit var computerMoveGetter: ComputerMoveGetter

  @Autowired
  private lateinit var gameEndChecker: GameEndChecker

  fun createGameWithComputer(difficultyLevel: DifficultyLevel): GameWithComputerDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = gameComponent.getStartingPlayer()
    val game = gameComponent.createGameWithComputer(player.id, difficultyLevel, startingPlayer)
        .let(gameRepository::save)
    val moves = if (startingPlayer == GameTurn.COMPUTER) {
      val computerMove = setComputeMove(game.gameId!!, game.difficultyLevel, game.computerMark, listOf())
      listOf(computerMove)
    } else {
      listOf()
    }
    return updateGame(game, moves, game.playerMark, game.computerMark)
  }

  fun setPlayerMoveAndGetComputerMove(gameId: Long, fieldIndex: Int): GameWithComputerDto {
    val game = gameRepository.findGameByGameId(gameId)
    checkIfGameIsOnGoing(game)
    moveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val allMoves = moveFacade.getAllMoves(gameId)
    val updatedMoves = if (canDoNextMove(allMoves, game.playerMark, game.computerMark)) {
      val computerMove = setComputeMove(gameId, game.difficultyLevel, game.computerMark, allMoves)
      allMoves + listOf(computerMove)
    } else {
      allMoves
    }
    return updateGame(game, updatedMoves, game.playerMark, game.computerMark)
  }

  fun getGameResults(): List<GameResultDto> =
      gameRepository.findAllByStatusIn(GameStatus.getEndedGameStatus())
          .map(GameWithComputer::toResultDto)

  fun getGameResultDetails(gameId: Long): GameResultDetailsDto {
    val game = gameRepository.findGameByGameId(gameId)
    checkIfGameDidEnd(game)
    val moves = moveFacade.getAllMoves(gameId)
    return game.toResultDetailsDto(moves)
  }

  private fun updateGame(
    game: GameWithComputer,
    moves: List<MoveDto>,
    playerMark: FieldMark,
    computerMark: FieldMark
  ): GameWithComputerDto =
      game.apply {
        status = gameEndChecker.checkGameEnd(moves, playerMark, computerMark)
        modificationDate = DateTime.now().millis
      }.let(gameRepository::save)
          .toDto(moves)

  private fun setComputeMove(
    gameId: Long,
    difficultyLevel: DifficultyLevel,
    computerMark: FieldMark,
    moves: List<MoveDto>
  ): MoveDto {
    val computerMoveFieldIndex = computerMoveGetter.getComputerMove(difficultyLevel, computerMark, moves)
    return moveFacade.setMove(gameId, computerMoveFieldIndex, computerMark)
  }

  private fun canDoNextMove(moves: List<MoveDto>, playerMark: FieldMark, computerMark: FieldMark) =
      gameEndChecker.checkGameEnd(moves, playerMark, computerMark) == ON_GOING

  private fun checkIfGameDidEnd(game: GameWithComputer) {
    if (game.status !in GameStatus.getEndedGameStatus()) {
      throw GameIsOnGoingException()
    }
  }

  private fun checkIfGameIsOnGoing(game: GameWithComputer) {
    if (game.status != ON_GOING) {
      throw GameEndedException()
    }
  }
}