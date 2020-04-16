package com.piotr.tictactoe.move.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.move.domain.model.Move
import com.piotr.tictactoe.move.dto.MoveDto
import org.springframework.stereotype.Component

@Component
class GameMoveDtoConverter : Converter<Move, MoveDto> {

  override fun convert(from: Move) = MoveDto(
      moveId = from.moveId!!,
      fieldIndex = from.fieldIndex,
      counter = from.counter,
      mark = from.mark
  )
}