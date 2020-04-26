package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerGameResultDetailsConverter : ConverterWithArgs<SinglePlayerGameResultDto, SinglePlayerGameResultDetailsDto, AllGameMovesDto> {

  override fun convert(from: SinglePlayerGameResultDto, args: AllGameMovesDto) = SinglePlayerGameResultDetailsDto(
      gameId = from.gameId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      startDate = from.startDate,
      endDate = from.endDate,
      moves = args.moves
  )
}