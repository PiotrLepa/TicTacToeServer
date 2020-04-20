package com.piotr.tictactoe.singlePlayerGame.domain.utils.computerMoveLogic

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.utils.GameConstant
import com.piotr.tictactoe.utils.GameConstant.VALID_FIELDS_INDEXES
import com.piotr.tictactoe.utils.GameEndChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ComputerMoveLogic @Autowired constructor(
  private val gameEndChecker: GameEndChecker
) {

  private lateinit var playerMark: FieldMark
  private lateinit var computerMark: FieldMark

  fun calculateComputerMove(difficultyLevel: DifficultyLevel, computerMark: FieldMark, moves: List<GameMoveDto>): Int {
    this.computerMark = computerMark
    this.playerMark = getOppositeMark(computerMark)

    val computerMove = when (difficultyLevel) {
      EASY -> minMax(moves, computerMark, EASY_MODE_CALLS)
      MEDIUM -> minMax(moves, computerMark, MEDIUM_MODE_CALLS)
      HARD -> minMax(moves, computerMark, HARD_MODE_CALLS)
    }

    return computerMove.index
  }

  private fun minMax(moves: List<GameMoveDto>, mark: FieldMark, maxCalls: Int): MinMaxMove {
    val availableSpots = getAvailableSpotsIndexes(moves)
    if (maxCalls == 0) {
      return MinMaxMove(0, getRandomSpotOrError(availableSpots))
    }
    if (isFirsMove(availableSpots)) {
      return MinMaxMove(0, availableSpots.random())
    }

    return when {
      gameEndChecker.checkWin(moves, playerMark) -> MinMaxMove(-1, ERROR_FIELD_INDEX)
      gameEndChecker.checkWin(moves, computerMark) -> MinMaxMove(1, ERROR_FIELD_INDEX)
      gameEndChecker.checkDraw(moves) -> MinMaxMove(0, ERROR_FIELD_INDEX)
      else -> {
        val minMaxMoves = availableSpots.map { spot ->
          val newMove = GameMoveDto(-1, spot, -1, mark)
          val newMoves = moves + listOf(newMove)
          val move = minMax(newMoves, getOppositeMark(mark), maxCalls - 1)
          MinMaxMove(move.score, spot)
        }

        if (mark == computerMark) {
          minMaxMoves.maxBy { it.score }!!
        } else {
          minMaxMoves.minBy { it.score }!!
        }
      }
    }
  }

  private fun getRandomSpotOrError(availableSpots: List<Int>) = if (availableSpots.isNotEmpty()) {
    availableSpots.random()
  } else {
    ERROR_FIELD_INDEX
  }

  private fun isFirsMove(availableSpots: List<Int>) = availableSpots.size == GameConstant.FIELD_MAX_INDEX + 1

  private fun getOppositeMark(mark: FieldMark) = if (mark == FieldMark.X) FieldMark.O else FieldMark.X

  private fun getAvailableSpotsIndexes(moves: List<GameMoveDto>): List<Int> =
      VALID_FIELDS_INDEXES.filter { i -> moves.find { it.fieldIndex == i } == null }

  companion object {
    private const val ERROR_FIELD_INDEX = -1
    private const val EASY_MODE_CALLS = 2
    private const val MEDIUM_MODE_CALLS = 3
    private const val HARD_MODE_CALLS = Int.MAX_VALUE
  }
}