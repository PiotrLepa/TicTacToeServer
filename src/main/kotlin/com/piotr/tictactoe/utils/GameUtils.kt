package com.piotr.tictactoe.utils

import com.piotr.tictactoe.domain.dto.FieldDto
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.GameStatus
import com.piotr.tictactoe.domain.dto.Mark

object GameUtils {

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

  fun isGameEnded(gameDto: GameDto): Boolean =
      isDraw(gameDto)
          || checkWin(gameDto.board, Mark.X)
          || checkWin(gameDto.board, Mark.O)

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

  fun getAvailableSpotsIndexes(board: List<FieldDto>): List<Int> =
      board.filter { it.mark == Mark.EMPTY }.map { it.index }

  fun setAiMoveToBoard(gameDto: GameDto, fieldDto: FieldDto) {
    gameDto.board[fieldDto.index].mark = fieldDto.mark
  }

  fun setGameEnded(gameDto: GameDto) {
    gameDto.status = GameStatus.ENDED
  }
}