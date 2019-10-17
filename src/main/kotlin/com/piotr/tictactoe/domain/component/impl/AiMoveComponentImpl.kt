package com.piotr.tictactoe.domain.component.impl

import com.piotr.tictactoe.domain.component.AiMoveComponent
import com.piotr.tictactoe.domain.dto.DifficultyLevel.EASY
import com.piotr.tictactoe.domain.dto.DifficultyLevel.HARD
import com.piotr.tictactoe.domain.dto.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.domain.dto.FieldDto
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.GameStatus
import com.piotr.tictactoe.domain.dto.Mark
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.Random

@Component
class AiMoveComponentImpl : AiMoveComponent {

  private lateinit var playerMark: Mark
  private lateinit var aiMark: Mark

  override fun setFieldByAi(gameDto: GameDto): GameDto {
    playerMark = gameDto.playerMark
    aiMark = gameDto.aiMark

    val aiMove = when (gameDto.difficultyLevel) {
      EASY -> minMax(gameDto.board, gameDto.aiMark, 2)
      MEDIUM -> minMax(gameDto.board, gameDto.aiMark, 3)
      HARD -> minMax(gameDto.board, gameDto.aiMark, 999999999)
    }

    if (aiMove.index != -1) {
      setAiMoveToBoard(gameDto, aiMove)
    } else {
      gameDto.gameStatus = GameStatus.GAME_ENDED
    }
    return gameDto
  }

  private fun setAiMoveToBoard(gameDto: GameDto, move: Move) {
    gameDto.board[move.index].mark = aiMark
  }

  private fun minMax(board: List<FieldDto>, playerMark: Mark, maxCalls: Int): Move {
    if (maxCalls == 0) {
      return Move(0, -1)
    }

    val availSpots = getAvailableSpots(board)
    if (availSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random().nextInt(availSpots.size)
      return Move(0, availSpots[random].index)
    }

    if (checkWin(board, playerMark)) {
      return Move(-1, -1)
    } else if (checkWin(board, aiMark)) {
      return Move(1, -1)
    } else if (availSpots.isEmpty()) {
      return Move(0, -1)
    }

    val moves = ArrayList<Move>()

    for (availSpot in availSpots) {
      val fieldIndex = board[availSpot.index].index

      board[availSpot.index].mark = playerMark

      val score = if (playerMark == aiMark) {
        minMax(board, playerMark, maxCalls - 1).score
      } else {
        minMax(board, aiMark, maxCalls - 1).score
      }

      board[availSpot.index].mark = Mark.EMPTY

      moves.add(Move(fieldIndex, score))
    }

    var bestMove = 0
    if (playerMark == aiMark) {
      var bestScore = -10000
      for (move in moves) {
        if (move.score > bestScore) {
          bestScore = move.score
          bestMove = move.index
        }
      }
    } else {
      var bestScore = 10000
      for (move in moves) {
        if (move.score > bestScore) {
          bestScore = move.score
          bestMove = move.index
        }
      }
    }

    return moves[bestMove]
  }

  private fun getAvailableSpots(board: List<FieldDto>) =
      board.filter { it.mark == Mark.EMPTY }

  private fun checkWin(board: List<FieldDto>, mark: Mark): Boolean {
    for (combination in WINNING_COMBINATIONS) {
      if (board[combination[0]].mark == mark
          && board[combination[1]].mark == mark
          && board[combination[2]].mark == mark) {
        return true
      }
    }

    return false
  }

  private inner class Move(
    var score: Int,
    var index: Int
  )

  companion object {
    val WINNING_COMBINATIONS = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
  }
}