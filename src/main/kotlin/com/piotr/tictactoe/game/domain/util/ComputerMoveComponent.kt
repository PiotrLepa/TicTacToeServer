package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.GameAlreadyEndedComputerWon
import com.piotr.tictactoe.game.domain.GameAlreadyEndedDraw
import com.piotr.tictactoe.game.domain.GameAlreadyEndedPlayerWon
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.logic.extension.replace
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.stereotype.Component
import java.util.Random

@Component
class ComputerMoveComponent {

  private lateinit var playerMark: FieldMark
  private lateinit var computerMark: FieldMark

  fun getComputerMove(game: GameWithComputerDto, moves: List<MoveDto>): Int {
    playerMark = game.playerMark
    computerMark = game.computerMark

    if (checkGameEnd(game, moves)) {
      throw getProperException(game, moves)
    }

    val computerMove = when (game.difficultyLevel) {
      EASY -> minMax(moves, game.computerMark, 2)
      MEDIUM -> minMax(moves, game.computerMark, 3)
      HARD -> minMax(moves, game.computerMark, Int.MAX_VALUE)
    }

    return if (computerMove.index != ERROR_FIELD_INDEX) {
      computerMove.index
    } else {
      throw getProperException(game, moves)
    }
  }

  private fun checkGameEnd(game: GameWithComputerDto, moves: List<MoveDto>): Boolean =
      when {
        checkWin(moves, game.playerMark) -> true
        checkWin(moves, game.computerMark) -> true
        checkDraw(moves) -> true
        else -> false
      }

  private fun getProperException(game: GameWithComputerDto, moves: List<MoveDto>): Exception =
      when {
        checkWin(moves, game.playerMark) -> GameAlreadyEndedPlayerWon()
        checkWin(moves, game.computerMark) -> GameAlreadyEndedComputerWon()
        else -> GameAlreadyEndedDraw()
      }

  private fun minMax(moves: List<MoveDto>, playerMark: FieldMark, maxCalls: Int): MinMaxMove {
    if (maxCalls == 0) {
      return MinMaxMove(0, -1)
    }

    val availableSpots = getAvailableSpotsIndexes(moves)
    if (availableSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random().nextInt(availableSpots.size)
      return MinMaxMove(0, availableSpots[random])
    }

    if (checkWin(moves, playerMark)) {
      return MinMaxMove(-1, ERROR_FIELD_INDEX)
    } else if (checkWin(moves, computerMark)) {
      return MinMaxMove(1, ERROR_FIELD_INDEX)
    } else if (availableSpots.isEmpty()) {
      return MinMaxMove(0, ERROR_FIELD_INDEX)
    }

    val minMaxMoves = mutableListOf<MinMaxMove>()

    for (spot in availableSpots) {
      val old = moves[spot]
      val newMoves = moves.replace(old, old.copy(mark = playerMark))

      val move = if (playerMark == computerMark) {
        minMax(newMoves, this.playerMark, maxCalls - 1)
      } else {
        minMax(newMoves, computerMark, maxCalls - 1)
      }

//      moves.replace(old, old.copy(mark = FieldMark.EMPTY))

      minMaxMoves.add(MinMaxMove(move.score, spot))
    }

    return if (playerMark == computerMark) {
      minMaxMoves.maxBy { it.score }!!
    } else {
      minMaxMoves.minBy { it.score }!!
    }
  }

  private fun getAvailableSpotsIndexes(moves: List<MoveDto>): List<Int> {
    val availSpots = mutableListOf<Int>()
    (0..9).forEach { i ->
      if (moves.find { it.fieldIndex == i } == null) {
        availSpots.add(i)
      }
    }
    return availSpots
  }

  private fun checkWin(moves: List<MoveDto>, mark: FieldMark): Boolean {
    for (combination in winningCombinations) {
      if (moves.getOrNull(combination[0])?.mark == mark
          && moves.getOrNull(combination[1])?.mark == mark
          && moves.getOrNull(combination[2])?.mark == mark) {
        return true
      }
    }
    return false
  }

  private fun checkDraw(moves: List<MoveDto>): Boolean = moves.size == 9

  private inner class MinMaxMove(
    val score: Int,
    val index: Int
  )

  companion object {
    private const val ERROR_FIELD_INDEX = -1
    private val winningCombinations = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
  }
}