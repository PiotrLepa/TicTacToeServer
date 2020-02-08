package com.piotr.tictactoe.move.dto

import com.piotr.tictactoe.move.domain.model.FieldMark

data class MoveDto(
  val moveId: Long,
  val fieldIndex: Int,
  val counter: Int,
  val mark: FieldMark
)