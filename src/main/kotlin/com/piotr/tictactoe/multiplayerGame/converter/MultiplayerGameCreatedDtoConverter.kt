package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameCreatedDtoConverter :
    ConverterWithArgs<MultiplayerGame, MultiplayerGameCreatedDto, MultiplayerGameCreatedDtoConverterArgs> {

  override fun convert(from: MultiplayerGame, args: MultiplayerGameCreatedDtoConverterArgs) = MultiplayerGameCreatedDto(
      gameId = from.gameId!!,
      yourMark = args.yourMark,
      playerType = args.playerType
  )
}