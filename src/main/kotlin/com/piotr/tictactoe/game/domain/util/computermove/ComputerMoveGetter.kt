package com.piotr.tictactoe.game.domain.util.computermove

import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.domain.model.GameStatus.COMPUTER_WON
import com.piotr.tictactoe.game.domain.model.GameStatus.DRAW
import com.piotr.tictactoe.game.domain.model.GameStatus.ON_GOING
import com.piotr.tictactoe.game.domain.model.GameStatus.PLAYER_WON
import com.piotr.tictactoe.game.domain.util.GameConstant.VALID_FIELDS_INDEXES
import com.piotr.tictactoe.game.domain.util.GameEndChecker
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.random.Random

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
      ON_GOING -> ComputerMove(ON_GOING, computerMove.index)
      PLAYER_WON -> ComputerMove(ON_GOING, ERROR_FIELD_INDEX)
      COMPUTER_WON -> ComputerMove(ON_GOING, ERROR_FIELD_INDEX)
      DRAW -> ComputerMove(ON_GOING, ERROR_FIELD_INDEX)
    }
  }

  private fun minMax(moves: List<MoveDto>, mark: FieldMark, maxCalls: Int): MinMaxMove {
    if (maxCalls == 0) {
      return MinMaxMove(0, -1)
    }

    val availableSpots = getAvailableSpotsIndexes(moves)
    if (availableSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      val random = Random.nextInt(availableSpots.size)
      return MinMaxMove(0,
          availableSpots[random])
    }

    return when {
      gameEndChecker.checkWin(moves, mark) -> MinMaxMove(-1, ERROR_FIELD_INDEX)
      gameEndChecker.checkWin(moves, computerMark) -> MinMaxMove(1, ERROR_FIELD_INDEX)
      availableSpots.isEmpty() -> MinMaxMove(0, ERROR_FIELD_INDEX)
      else -> {
        val minMaxMoves = mutableListOf<MinMaxMove>()
        for (spot in availableSpots) {
          val newMove = MoveDto(-1, -1, spot, -1, mark)
          val newMoves = moves + listOf(newMove)
          val move = if (mark == computerMark) {
            minMax(newMoves, playerMark, maxCalls - 1)
          } else {
            minMax(newMoves, computerMark, maxCalls - 1)
          }
          minMaxMoves.add(MinMaxMove(move.score, spot))
        }

        if (mark == computerMark) {
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