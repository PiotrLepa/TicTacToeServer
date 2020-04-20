package com.piotr.tictactoe.utils

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import org.springframework.stereotype.Component

@Component
class GameEndChecker {

  fun checkGameEnd(moves: List<GameMoveDto>, mark: FieldMark): GameResultType {
    val oppositeMark = if (mark == FieldMark.X) FieldMark.O else FieldMark.X
    return when {
      checkWin(moves, mark) -> GameResultType.WON
      checkWin(moves, oppositeMark) -> GameResultType.LOST
      checkDraw(moves) -> GameResultType.DRAW
      else -> GameResultType.ON_GOING
    }
  }

  fun checkWin(moves: List<GameMoveDto>, mark: FieldMark): Boolean {
    for (combination in winningCombinations) {
      if (moves.find { it.fieldIndex == combination[0] }?.mark == mark
          && moves.find { it.fieldIndex == combination[1] }?.mark == mark
          && moves.find { it.fieldIndex == combination[2] }?.mark == mark) {
        return true
      }
    }
    return false
  }

  fun checkDraw(moves: List<GameMoveDto>): Boolean = moves.size == 9 && !checkWin(moves, FieldMark.O) && !checkWin(moves, FieldMark.X)

  companion object {
    private val winningCombinations = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
  }

  enum class GameResultType {
    WON,
    LOST,
    DRAW,
    ON_GOING
  }
}