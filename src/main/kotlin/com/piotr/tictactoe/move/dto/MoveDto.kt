package com.piotr.tictactoe.move.dto

import com.piotr.tictactoe.move.domain.model.FieldMark

data class MoveDto(
  val id: Long,
  val gameId: Long,
  val fieldIndex: Int,
  val counter: Int,
  val mark: FieldMark
)