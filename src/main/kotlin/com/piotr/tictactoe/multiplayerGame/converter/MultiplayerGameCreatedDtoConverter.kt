package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameCreatedDtoConverter : Converter1<MultiplayerGame, MultiplayerGameCreatedDto, FieldMark> {

  override fun convert(from: MultiplayerGame, param1: FieldMark) = MultiplayerGameCreatedDto(
      gameId = from.gameId!!,
      yourMark = param1
  )
}