package com.piotr.tictactoe.domain.dto

data class PlayerMoveDto(
  val gameId: Long,
  val field: FieldDto
)