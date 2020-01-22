package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.Mark
import com.piotr.tictactoe.game.dto.FieldDto
import com.piotr.tictactoe.game.dto.GameDto

object GameEndChecker {

  private val WINNING_COMBINATIONS = arrayOf(
      intArrayOf(0, 1, 2),
      intArrayOf(3, 4, 5),
      intArrayOf(6, 7, 8),
      intArrayOf(0, 3, 6),
      intArrayOf(1, 4, 7),
      intArrayOf(2, 5, 8),
      intArrayOf(0, 4, 8),
      intArrayOf(2, 4, 6)
  )

  fun isDraw(gameDto: GameDto) =
      gameDto.board.none { it.mark == Mark.EMPTY }

  fun checkWin(board: List<FieldDto>, mark: Mark): Boolean {
    for (combination in WINNING_COMBINATIONS) {
      if (board[combination[0]].mark == mark
          && board[combination[1]].mark == mark
          && board[combination[2]].mark == mark) {
        return true
      }
    }
    return false
  }
}