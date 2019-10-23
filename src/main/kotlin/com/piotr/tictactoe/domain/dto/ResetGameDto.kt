package com.piotr.tictactoe.domain.dto

data class ResetGameDto(
  val gameId: Long,
  val difficultyLevel: DifficultyLevel
)