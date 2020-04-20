package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.Converter2
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.PlayerType
import org.springframework.stereotype.Component

@Component
class MultiplayerGameCreatedDtoConverter : Converter2<MultiplayerGame, MultiplayerGameCreatedDto, FieldMark, PlayerType> {

  override fun convert(from: MultiplayerGame, param1: FieldMark, param2: PlayerType) = MultiplayerGameCreatedDto(
      gameId = from.gameId!!,
      yourMark = param1,
      playerType = param2
  )
}