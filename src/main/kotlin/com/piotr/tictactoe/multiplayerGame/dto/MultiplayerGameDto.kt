package com.piotr.tictactoe.multiplayerGame.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus

data class MultiplayerGameDto(
  val gameId: Long,
  val yourMark: FieldMark,
  val opponentMark: FieldMark,
  val status: MultiplayerGameStatus,
  val currentTurn: MultiplayerGameTurnDto,
  val moves: List<GameMoveDto>
)