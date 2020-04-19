package com.piotr.tictactoe.gameResult.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import java.sql.Timestamp

data class GameResultDto(
  val gameId: Long,
  val playerId: Long,
  val status: SinglePlayerGameStatus,
  val difficultyLevel: DifficultyLevel,
  var playerMark: FieldMark,
  var computerMark: FieldMark,
  val startDate: Timestamp,
  val endDate: Timestamp
)