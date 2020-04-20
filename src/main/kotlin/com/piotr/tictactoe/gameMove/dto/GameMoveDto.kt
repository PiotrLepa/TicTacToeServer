package com.piotr.tictactoe.gameMove.dto

import com.piotr.tictactoe.gameMove.domain.model.FieldMark

data class GameMoveDto(
  val moveId: Long,
  val fieldIndex: Int,
  val counter: Int,
  val mark: FieldMark
)