package com.piotr.tictactoe.gameMove.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameMove.domain.model.GameMove
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import org.springframework.stereotype.Component

@Component
class GameMoveDtoConverter : Converter<GameMove, GameMoveDto> {

  override fun convert(from: GameMove) = GameMoveDto(
      moveId = from.moveId!!,
      fieldIndex = from.fieldIndex,
      counter = from.counter,
      mark = from.mark
  )
}