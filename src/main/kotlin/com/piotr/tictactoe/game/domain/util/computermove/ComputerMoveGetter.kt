package com.piotr.tictactoe.game.domain.util.computermove

import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.domain.model.GameStatus.COMPUTER_WON
import com.piotr.tictactoe.game.domain.model.GameStatus.DRAW
import com.piotr.tictactoe.game.domain.model.GameStatus.IN_PROGRESS
import com.piotr.tictactoe.game.domain.model.GameStatus.PLAYER_WON
import com.piotr.tictactoe.game.domain.util.GameConstant.VALID_FIELDS_INDEXES
import com.piotr.tictactoe.game.domain.util.GameEndChecker
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.logic.extension.replace
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Random

@Component
class ComputerMoveGetter {

  @Autowired
  private lateinit var gameEndChecker: GameEndChecker

  private lateinit var playerMark: FieldMark
  private lateinit var computerMark: FieldMark

  fun getComputerMove(game: GameWithComputerDto): ComputerMove {
    playerMark = game.playerMark
    computerMark = game.computerMark

    val computerMove = when (game.difficultyLevel) {
      EASY -> minMax(game.moves, game.computerMark, 2)
      MEDIUM -> minMax(game.moves, game.computerMark, 3)
      HARD -> minMax(game.moves, game.computerMark, Int.MAX_VALUE)
    }

    return when (gameEndChecker.checkGameEnd(game)) {
      IN_PROGRESS -> ComputerMove(IN_PROGRESS,
          computerMove.index)
      PLAYER_WON -> ComputerMove(IN_PROGRESS,
          ERROR_FIELD_INDEX)
      COMPUTER_WON -> ComputerMove(IN_PROGRESS,
          ERROR_FIELD_INDEX)
      DRAW -> ComputerMove(IN_PROGRESS,
          ERROR_FIELD_INDEX)
    }
  }

  private fun minMax(moves: List<MoveDto>, playerMark: FieldMark, maxCalls: Int): MinMaxMove {
    if (maxCalls == 0) {
      return MinMaxMove(0, -1)
    }

    val availableSpots = getAvailableSpotsIndexes(moves)
    if (availableSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random().nextInt(availableSpots.size)
      return MinMaxMove(0,
          availableSpots[random])
    }

    return when {
      gameEndChecker.checkWin(moves, playerMark) -> MinMaxMove(-1, ERROR_FIELD_INDEX)
      gameEndChecker.checkWin(moves, computerMark) -> MinMaxMove(1, ERROR_FIELD_INDEX)
      availableSpots.isEmpty() -> MinMaxMove(0, ERROR_FIELD_INDEX)
      else -> {
        val minMaxMoves = mutableListOf<MinMaxMove>()

        for (spot in availableSpots) {
          val old = moves[spot]
          val newMoves = moves.replace(old, old.copy(mark = playerMark))
          val move = if (playerMark == computerMark) {
            minMax(newMoves, this.playerMark, maxCalls - 1)
          } else {
            minMax(newMoves, computerMark, maxCalls - 1)
          }
          minMaxMoves.add(MinMaxMove(move.score, spot))
        }

        if (playerMark == computerMark) {
          minMaxMoves.maxBy { it.score }!!
        } else {
          minMaxMoves.minBy { it.score }!!
        }
      }
    }

  }

  private fun getAvailableSpotsIndexes(moves: List<MoveDto>): List<Int> {
    val availSpots = mutableListOf<Int>()
    VALID_FIELDS_INDEXES.forEach { i ->
      if (moves.find { it.fieldIndex == i } == null) {
        availSpots.add(i)
      }
    }
    return availSpots
  }

  companion object {
    private const val ERROR_FIELD_INDEX = -1
  }
}