package com.piotr.tictactoe.singlePlayerGame.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus

data class SinglePlayerGameDetailsDto(
  val gameId: Long,
  val playerId: Long,
  val status: SinglePlayerGameStatus,
  val difficultyLevel: DifficultyLevel,
  var playerMark: FieldMark,
  var computerMark: FieldMark,
  val startDate: Long,
  val endDate: Long
)