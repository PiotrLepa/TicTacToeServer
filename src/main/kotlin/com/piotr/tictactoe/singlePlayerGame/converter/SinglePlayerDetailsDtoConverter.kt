package com.piotr.tictactoe.singlePlayerGame.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import org.springframework.stereotype.Component

@Component
class SinglePlayerDetailsDtoConverter : Converter<SinglePlayerGame, SinglePlayerGameResultDto> {

  override fun convert(from: SinglePlayerGame) = SinglePlayerGameResultDto(
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