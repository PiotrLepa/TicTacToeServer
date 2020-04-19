package com.piotr.tictactoe.multiplayerGame.dto

import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn

data class MultiplayerGameDto(
  val gameId: Long,
  val status: MultiplayerGameStatus,
  val currentTurn: MultiplayerGameTurn,
  val moves: List<GameMoveDto>
)