package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerGameResultDetailsConverter : Converter1<SinglePlayerGameResultDto, SinglePlayerGameResultDetailsDto, AllGameMovesDto> {

  override fun convert(from: SinglePlayerGameResultDto, param1: AllGameMovesDto) = SinglePlayerGameResultDetailsDto(
      gameId = from.gameId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      startDate = from.startDate,
      endDate = from.endDate,
      moves = param1.moves
  )
}