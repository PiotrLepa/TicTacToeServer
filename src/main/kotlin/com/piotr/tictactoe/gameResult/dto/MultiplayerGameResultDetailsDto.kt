package com.piotr.tictactoe.gameResult.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import java.sql.Timestamp

data class MultiplayerGameResultDetailsDto(
  val gameId: Long,
  val status: MultiplayerGameStatus,
  var firstPlayerMark: FieldMark,
  var secondPlayerMark: FieldMark,
  val startDate: Timestamp,
  val endDate: Timestamp,
  val moves: List<GameMoveDto>
)