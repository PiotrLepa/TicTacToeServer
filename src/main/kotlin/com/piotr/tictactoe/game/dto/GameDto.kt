package com.piotr.tictactoe.game.dto

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