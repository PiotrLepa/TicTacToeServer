package com.piotr.tictactoe.singlePlayerGame.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerDetailsDtoConverter : Converter<SinglePlayerGame, SinglePlayerGameDetailsDto> {

  override fun convert(from: SinglePlayerGame) = SinglePlayerGameDetailsDto(
      gameId = from.gameId!!,
      playerId = from.playerId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      startDate = from.creationDate,
      endDate = from.modificationDate
  )
}