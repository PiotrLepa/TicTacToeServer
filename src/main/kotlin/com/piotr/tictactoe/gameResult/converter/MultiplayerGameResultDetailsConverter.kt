package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameResultDetailsConverter : ConverterWithArgs<MultiplayerGameResultDto, MultiplayerGameResultDetailsDto, AllGameMovesDto> {

  override fun convert(from: MultiplayerGameResultDto, args: AllGameMovesDto) = MultiplayerGameResultDetailsDto(
      gameId = from.gameId,
      status = from.status,
      firstPlayerMark = from.firstPlayerMark,
      secondPlayerMark = from.secondPlayerMark,
      startDate = from.startDate,
      endDate = from.endDate,
      moves = args.moves
  )
}