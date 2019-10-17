package com.piotr.tictactoe.domain.dto

data class GameDto(
  val gameId: Long,
  var gameStatus: GameStatus,
  val difficultyLevel: DifficultyLevel,
  val board: List<FieldDto>,
  val playerMark: Mark,
  val aiMark: Mark
)