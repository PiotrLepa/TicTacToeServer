package com.piotr.tictactoe.domain.component.impl

import com.piotr.tictactoe.domain.component.AiMoveComponent
import com.piotr.tictactoe.domain.dto.DifficultyLevel.EASY
import com.piotr.tictactoe.domain.dto.DifficultyLevel.HARD
import com.piotr.tictactoe.domain.dto.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.domain.dto.FieldDto
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.Mark
import com.piotr.tictactoe.utils.GameUtils.checkWin
import com.piotr.tictactoe.utils.GameUtils.getAvailableSpotsIndexes
import com.piotr.tictactoe.utils.GameUtils.setAiMoveToBoard
import com.piotr.tictactoe.utils.GameUtils.setGameEnded
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.Random

@Component
class AiMoveComponentImpl : AiMoveComponent {

  private lateinit var humanMark: Mark
  private lateinit var aiMark: Mark

  override fun setFieldByAi(gameDto: GameDto): GameDto {
    humanMark = gameDto.playerMark
    aiMark = gameDto.aiMark

    val aiMove = when (gameDto.difficultyLevel) {
      EASY -> minMax(gameDto.board, gameDto.aiMark, 2)
      MEDIUM -> minMax(gameDto.board, gameDto.aiMark, 3)
      HARD -> minMax(gameDto.board, gameDto.aiMark, Int.MAX_VALUE)
    }

    if (aiMove.index != -1) {
      setAiMoveToBoard(gameDto, FieldDto(aiMove.index, aiMark))
    } else {
      // game ended
      setGameEnded(gameDto)
    }
    return gameDto
  }

  private fun minMax(board: List<FieldDto>, playerMark: Mark, maxCalls: Int): Move {
    if (maxCalls == 0) {
      return Move(0, -1)
    }

    val availSpots = getAvailableSpotsIndexes(board)
    if (availSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random().nextInt(availSpots.size)
      return Move(0, availSpots[random])
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
      board[availSpot].mark = playerMark

      val move = if (playerMark == aiMark) {
        minMax(board, humanMark, maxCalls - 1)
      } else {
        minMax(board, aiMark, maxCalls - 1)
      }

      board[availSpot].mark = Mark.EMPTY

      moves.add(Move(move.score, availSpot))
    }

    return if (playerMark == aiMark) {
      moves.maxBy { it.score }!!
    } else {
      moves.minBy { it.score }!!
    }
  }

  private inner class Move(
    var score: Int,
    var index: Int
  )
}