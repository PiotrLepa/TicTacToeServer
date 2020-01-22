package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.FieldDto
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Field(
  @Id
  val id: Long?,
  val index: Int,
  var mark: Mark
) {

  fun toDto() = FieldDto(
      index = index,
      mark = mark
  )

  companion object {
    fun fromDto(dto: FieldDto) = Field(
        id = null,
        index = dto.index,
        mark = dto.mark
    )
  }
}