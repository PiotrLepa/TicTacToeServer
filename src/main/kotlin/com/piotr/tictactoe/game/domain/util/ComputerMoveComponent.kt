package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.stereotype.Component
import java.util.Random

@Component
class ComputerMoveComponent {

  private lateinit var playerMark: FieldMark
  private lateinit var computerMark: FieldMark

  fun getComputerMove(game: GameWithComputerDto, moves: List<MoveDto>): GameWithComputerDto {
    playerMark = game.playerMark
    computerMark = game.computerMark

    if (checkGameEnd(game)) {
      return game
    }

    val computerMove = when (game.difficultyLevel) {
      EASY -> minMax(moves, game.computerMark, 2)
      MEDIUM -> minMax(moves, game.computerMark, 3)
      HARD -> minMax(moves, game.computerMark, Int.MAX_VALUE)
    }

//    if (aiMove.index != -1) {
//      setAiMoveToBoard(game, Field(null, aiMove.index, computerMark))
//    } else {
//      checkGameEnd(game)
//    }

    checkGameEnd(game)

    return game
  }

  private fun checkGameEnd(game: GameWithComputerDto): Boolean = game.let {
    when {
      isDraw(game) -> {
        it.draws++
        it.status = GameStatus.DRAW
        true
      }
      checkWin(it.board, playerMark) -> {
        it.playerWins++
        it.status = GameStatus.PLAYER_WON
        true
      }
      checkWin(it.board, computerMark) -> {
        it.playerDefeats++
        it.status = GameStatus.PLAYER_DEFEAT
        true
      }
      else -> false
    }
  }

  private fun minMax(moves: List<MoveDto>, playerMark: FieldMark, maxCalls: Int): MinMaxMove {
    if (maxCalls == 0) {
      return MinMaxMove(0, -1)
    }

    val availSpots = getAvailableSpotsIndexes(moves)
    if (availSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random().nextInt(availSpots.size)
      return MinMaxMove(0, availSpots[random])
    }

    if (checkWin(moves, playerMark)) {
      return MinMaxMove(-1, -1)
    } else if (checkWin(moves, computerMark)) {
      return MinMaxMove(1, -1)
    } else if (availSpots.isEmpty()) {
      return MinMaxMove(0, -1)
    }

    val newMoves = mutableListOf<MinMaxMove>()

    for (availSpot in availSpots) {
      moves[availSpot].mark = playerMark

      val move = if (playerMark == computerMark) {
        minMax(moves, this.playerMark, maxCalls - 1)
      } else {
        minMax(moves, computerMark, maxCalls - 1)
      }

      moves[availSpot].mark = ""

      moves.add(MinMaxMove(move.score, availSpot))
    }

    return if (playerMark == computerMark) {
      newMoves.maxBy { it.score }!!
    } else {
      newMoves.minBy { it.score }!!
    }
  }

  private fun getAvailableSpotsIndexes(board: List<MoveDto>): List<Int> =
      board.filter { it.mark == Mark.EMPTY }.map { it.index }

  private fun checkWin(fields: List<MoveDto>, mark: FieldMark): Boolean {
    for (combination in winningCombinations) {
      if (fields.getOrNull(combination[0])?.mark == mark
          && fields.getOrNull(combination[1])?.mark == mark
          && fields.getOrNull(combination[2])?.mark == mark) {
        return true
      }
    }
    return false
  }

  private inner class MinMaxMove(
    var score: Int,
    var index: Int
  )

  companion object {
    private val winningCombinations = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
  }
}