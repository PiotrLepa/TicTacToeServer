package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.util.GameEndChecker.checkWin
import com.piotr.tictactoe.game.domain.util.GameEndChecker.isDraw
import com.piotr.tictactoe.game.dto.DifficultyLevel.EASY
import com.piotr.tictactoe.game.dto.DifficultyLevel.HARD
import com.piotr.tictactoe.game.dto.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.dto.FieldDto
import com.piotr.tictactoe.game.dto.GameDto
import com.piotr.tictactoe.game.dto.GameStatus
import com.piotr.tictactoe.game.dto.Mark
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.Random

@Component
class AiMoveComponent {

  private lateinit var humanMark: Mark
  private lateinit var aiMark: Mark

  fun setFieldByAi(gameDto: GameDto): GameDto {
    humanMark = gameDto.playerMark
    aiMark = gameDto.aiMark

    if (checkGameEnd(gameDto)) {
      return gameDto
    }

    val aiMove = when (gameDto.difficultyLevel) {
      EASY -> minMax(gameDto.board, gameDto.aiMark, 2)
      MEDIUM -> minMax(gameDto.board, gameDto.aiMark, 3)
      HARD -> minMax(gameDto.board, gameDto.aiMark, Int.MAX_VALUE)
    }

    if (aiMove.index != -1) {
      setAiMoveToBoard(gameDto, FieldDto(aiMove.index, aiMark))
    } else {
      checkGameEnd(gameDto)
    }

    checkGameEnd(gameDto)

    return gameDto
  }

  private fun checkGameEnd(gameDto: GameDto): Boolean = gameDto.let {
    when {
      isDraw(gameDto) -> {
        it.draws++
        it.status = GameStatus.DRAW
        true
      }
      checkWin(it.board, humanMark) -> {
        it.playerWins++
        it.status = GameStatus.PLAYER_WON
        true
      }
      checkWin(it.board, aiMark) -> {
        it.playerDefeats++
        it.status = GameStatus.PLAYER_DEFEAT
        true
      }
      else -> false
    }
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

  private fun getAvailableSpotsIndexes(board: List<FieldDto>): List<Int> =
      board.filter { it.mark == Mark.EMPTY }.map { it.index }

  private fun setAiMoveToBoard(gameDto: GameDto, fieldDto: FieldDto) {
    gameDto.board[fieldDto.index].mark = fieldDto.mark
  }

  private inner class Move(
    var score: Int,
    var index: Int
  )
}