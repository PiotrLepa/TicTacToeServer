package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.FieldDto

data class Field(
  val index: Int,
  var mark: Mark
) {

  fun toDto() = FieldDto(
      index = index,
      mark = mark
  )

  companion object {
    fun fromDto(dto: FieldDto) = Field(
        index = dto.index,
        mark = dto.mark
    )
  }
}