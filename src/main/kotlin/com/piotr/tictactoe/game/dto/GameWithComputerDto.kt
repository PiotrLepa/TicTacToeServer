package com.piotr.tictactoe.game.dto

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.move.domain.model.FieldMark

data class GameWithComputerDto(
  val id: Long,
  val playerId: Long,
  val status: GameStatus,
  val difficultyLevel: DifficultyLevel,
  val currentTurn: GameTurn,
  var playerMark: FieldMark,
  var computerMark: FieldMark
)