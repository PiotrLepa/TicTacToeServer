package com.piotr.tictactoe.game.domain.util.computermove

import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
import com.piotr.tictactoe.game.domain.util.GameConstant.VALID_FIELDS_INDEXES
import com.piotr.tictactoe.game.domain.util.GameEndChecker
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ComputerMoveGetter {

  @Autowired
  private lateinit var gameEndChecker: GameEndChecker

  private lateinit var playerMark: FieldMark
  private lateinit var computerMark: FieldMark

  fun getComputerMove(game: GameWithComputerDto): Int {
    playerMark = game.playerMark
    computerMark = game.computerMark

    val computerMove = when (game.difficultyLevel) {
      EASY -> minMax(game.moves, game.computerMark, 2)
      MEDIUM -> minMax(game.moves, game.computerMark, 3)
      HARD -> minMax(game.moves, game.computerMark, Int.MAX_VALUE)
    }

    return computerMove.index
  }

  private fun minMax(moves: List<MoveDto>, mark: FieldMark, maxCalls: Int): MinMaxMove {
    val availableSpots = getAvailableSpotsIndexes(moves)
    if (maxCalls == 0) {
      return MinMaxMove(0, availableSpots.random())
    }
    if (availableSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
      return MinMaxMove(0, availableSpots.random())
    }

    return when {
      gameEndChecker.checkWin(moves, playerMark) -> MinMaxMove(-1, ERROR_FIELD_INDEX)
      gameEndChecker.checkWin(moves, computerMark) -> MinMaxMove(1, ERROR_FIELD_INDEX)
      gameEndChecker.checkDraw(moves) -> MinMaxMove(0, ERROR_FIELD_INDEX)
      else -> {
        val minMaxMoves = availableSpots.map { spot ->
          val newMove = MoveDto(-1, -1, spot, -1, mark)
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

  private fun getOppositeMark(mark: FieldMark) = if (mark == FieldMark.X) FieldMark.O else FieldMark.X

  private fun getAvailableSpotsIndexes(moves: List<MoveDto>): List<Int> =
      VALID_FIELDS_INDEXES.filter { i -> moves.find { it.fieldIndex == i } == null }

  companion object {
    private const val ERROR_FIELD_INDEX = -1
    private val LOGGER = LoggerFactory.getLogger(ComputerMoveGetter::class.java)
  }
}