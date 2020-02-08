package com.piotr.tictactoe.game.dto

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.move.domain.model.FieldMark

data class GameResultDto(
  val gameId: Long,
  val playerId: Long,
  val status: GameStatus,
  val difficultyLevel: DifficultyLevel,
  var playerMark: FieldMark,
  var computerMark: FieldMark,
  val startDate: Long,
  val endDate: Long
)