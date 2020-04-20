package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameDtoConverter : Converter1<MultiplayerGame, MultiplayerGameDto, AllGameMovesDto> {

  override fun convert(from: MultiplayerGame, param1: AllGameMovesDto) = MultiplayerGameDto(
      gameId = from.gameId!!,
      currentTurn = from.currentTurn,
      status = from.status,
      moves = param1.moves
  )
}