package com.piotr.tictactoe.move.dto

import com.piotr.tictactoe.move.domain.model.FieldMark

data class SetMoveDto(
  val gameId: Long,
  val fieldIndex: Int,
  val mark: FieldMark
)