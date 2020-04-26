package com.piotr.tictactoe.singlePlayerGame.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerDtoConverter : ConverterWithArgs<SinglePlayerGame, SinglePlayerGameDto, AllGameMovesDto> {

  override fun convert(from: SinglePlayerGame, args: AllGameMovesDto) = SinglePlayerGameDto(
      gameId = from.gameId!!,
      playerId = from.playerId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      moves = args.moves
  )
}