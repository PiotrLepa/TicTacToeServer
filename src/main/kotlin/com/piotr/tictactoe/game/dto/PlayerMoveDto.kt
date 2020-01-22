package com.piotr.tictactoe.game.dto

data class PlayerMoveDto(
  val gameId: Long,
  val field: FieldDto
)