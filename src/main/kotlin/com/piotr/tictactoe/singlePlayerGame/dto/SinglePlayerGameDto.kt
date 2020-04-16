package com.piotr.tictactoe.singlePlayerGame.dto

import com.piotr.tictactoe.common.game.model.FieldMark
import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel

data class SinglePlayerGameDto(
  val gameId: Long,
  val playerId: Long,
  val status: GameStatus,
  val difficultyLevel: DifficultyLevel,
  var playerMark: FieldMark,
  var computerMark: FieldMark,
  val moves: List<GameMoveDto>
)