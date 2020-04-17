package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameCreatedDtoConverter : Converter<MultiplayerGame, MultiplayerGameCreatedDto> {

  override fun convert(from: MultiplayerGame) = MultiplayerGameCreatedDto(
      gameId = from.gameId!!
  )
}