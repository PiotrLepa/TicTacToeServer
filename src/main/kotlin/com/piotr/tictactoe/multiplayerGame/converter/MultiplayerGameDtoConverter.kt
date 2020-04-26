package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameDtoConverter : ConverterWithArgs<MultiplayerGame, MultiplayerGameDto, AllGameMovesDto> {

  override fun convert(from: MultiplayerGame, args: AllGameMovesDto) = MultiplayerGameDto(
      gameId = from.gameId!!,
      currentTurn = from.currentTurn,
      status = from.status,
      moves = args.moves
  )
}