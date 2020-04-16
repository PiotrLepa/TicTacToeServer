package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameResult.dto.GameResultDto
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import org.springframework.stereotype.Component

@Component
class GameResultConverter : Converter<SinglePlayerGameDetailsDto, GameResultDto> {

  override fun convert(from: SinglePlayerGameDetailsDto) = GameResultDto(
      gameId = from.gameId,
      playerId = from.playerId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      startDate = from.startDate,
      endDate = from.endDate
  )
}