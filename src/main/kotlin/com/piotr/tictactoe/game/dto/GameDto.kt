package com.piotr.tictactoe.game.dto

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.Mark

data class GameDto(
  val gameId: Long,
  val difficultyLevel: DifficultyLevel,
  val board: List<FieldDto>,
  val playerMark: Mark,
  val aiMark: Mark,
  var status: GameStatus,
  var playerWins: Int,
  var playerDefeats: Int,
  var draws: Int
)