package com.piotr.tictactoe.gameResult.dto

import com.piotr.tictactoe.common.game.model.FieldMark
import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel

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