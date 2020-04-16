package com.piotr.tictactoe.singlePlayerGame.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.move.dto.AllMovesDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerDtoConverter : Converter1<SinglePlayerGame, SinglePlayerGameDto, AllMovesDto> {

  override fun convert(from: SinglePlayerGame, param1: AllMovesDto) = SinglePlayerGameDto(
      gameId = from.gameId!!,
      playerId = from.playerId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      moves = param1.moves
  )
}