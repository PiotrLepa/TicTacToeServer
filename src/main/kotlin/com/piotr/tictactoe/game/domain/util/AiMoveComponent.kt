//package com.piotr.tictactoe.game.domain.util
//
//import com.piotr.tictactoe.game.domain.model.DifficultyLevel.EASY
//import com.piotr.tictactoe.game.domain.model.DifficultyLevel.HARD
//import com.piotr.tictactoe.game.domain.model.DifficultyLevel.MEDIUM
//import com.piotr.tictactoe.game.domain.model.Field
//import com.piotr.tictactoe.game.domain.model.Game
//import com.piotr.tictactoe.game.domain.model.GameStatus
//import com.piotr.tictactoe.game.domain.model.Mark
//import com.piotr.tictactoe.game.domain.util.GameEndChecker.checkWin
//import com.piotr.tictactoe.game.domain.util.GameEndChecker.isDraw
//import org.springframework.stereotype.Component
//import java.util.ArrayList
//import java.util.Random
//
//@Component
//class AiMoveComponent {
//
//  private lateinit var humanMark: Mark
//  private lateinit var aiMark: Mark
//
//  fun setFieldByAi(game: Game): Game {
//    humanMark = game.playerMark
//    aiMark = game.aiMark
//
//    if (checkGameEnd(game)) {
//      return game
//    }
//
//    val aiMove = when (game.difficultyLevel) {
//      EASY -> minMax(game.board, game.aiMark, 2)
//      MEDIUM -> minMax(game.board, game.aiMark, 3)
//      HARD -> minMax(game.board, game.aiMark, Int.MAX_VALUE)
//    }
//
//    if (aiMove.index != -1) {
//      setAiMoveToBoard(game, Field(null, aiMove.index, aiMark))
//    } else {
//      checkGameEnd(game)
//    }
//
//    checkGameEnd(game)
//
//    return game
//  }
//
//  private fun checkGameEnd(game: Game): Boolean = game.let {
//    when {
//      isDraw(game) -> {
//        it.draws++
//        it.status = GameStatus.DRAW
//        true
//      }
//      checkWin(it.board, humanMark) -> {
//        it.playerWins++
//        it.status = GameStatus.PLAYER_WON
//        true
//      }
//      checkWin(it.board, aiMark) -> {
//        it.playerDefeats++
//        it.status = GameStatus.PLAYER_DEFEAT
//        true
//      }
//      else -> false
//    }
//  }
//
//  private fun minMax(board: List<Field>, playerMark: Mark, maxCalls: Int): Move {
//    if (maxCalls == 0) {
//      return Move(0, -1)
//    }
//
//    val availSpots = getAvailableSpotsIndexes(board)
//    if (availSpots.size >= 8 && maxCalls < 4) { // TODO hardcoded
//      val random = Random().nextInt(availSpots.size)
//      return Move(0, availSpots[random])
//    }
//
//    if (checkWin(board, playerMark)) {
//      return Move(-1, -1)
//    } else if (checkWin(board, aiMark)) {
//      return Move(1, -1)
//    } else if (availSpots.isEmpty()) {
//      return Move(0, -1)
//    }
//
//    val moves = ArrayList<Move>()
//
//    for (availSpot in availSpots) {
//      board[availSpot].mark = playerMark
//
//      val move = if (playerMark == aiMark) {
//        minMax(board, humanMark, maxCalls - 1)
//      } else {
//        minMax(board, aiMark, maxCalls - 1)
//      }
//
//      board[availSpot].mark = Mark.EMPTY
//
//      moves.add(Move(move.score, availSpot))
//    }
//
//    return if (playerMark == aiMark) {
//      moves.maxBy { it.score }!!
//    } else {
//      moves.minBy { it.score }!!
//    }
//  }
//
//  private fun getAvailableSpotsIndexes(board: List<Field>): List<Int> =
//      board.filter { it.mark == Mark.EMPTY }.map { it.index }
//
//  private fun setAiMoveToBoard(game: Game, field: Field) {
//    game.board[field.index].mark = field.mark
//  }
//
//  private inner class Move(
//    var score: Int,
//    var index: Int
//  )
//}