package com.piotr.tictactoe.utils

import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.stereotype.Component

@Component
class GameEndChecker {

  fun checkGameEnd(moves: List<MoveDto>, playerMark: FieldMark, computerMark: FieldMark): GameStatus =
      when {
        checkWin(moves, playerMark) -> GameStatus.PLAYER_WON
        checkWin(moves, computerMark) -> GameStatus.COMPUTER_WON
        checkDraw(moves) -> GameStatus.DRAW
        else -> GameStatus.ON_GOING
      }

  fun checkWin(moves: List<MoveDto>, mark: FieldMark): Boolean {
    for (combination in winningCombinations) {
      if (moves.find { it.fieldIndex == combination[0] }?.mark == mark
          && moves.find { it.fieldIndex == combination[1] }?.mark == mark
          && moves.find { it.fieldIndex == combination[2] }?.mark == mark) {
        return true
      }
    }
    return false
  }

  fun checkDraw(moves: List<MoveDto>): Boolean = moves.size == 9 && !checkWin(moves, FieldMark.O) && !checkWin(moves, FieldMark.X)

  companion object {
    private val winningCombinations = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
  }
}