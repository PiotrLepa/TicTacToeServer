package com.piotr.tictactoe.multiplayerGame.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark

data class MultiplayerGameCreatedDto(
  val gameId: Long,
  val socketDestination: String,
  val yourMark: FieldMark,
  val playerType: PlayerType
)