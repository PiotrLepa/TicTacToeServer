package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameResultDetailsConverter : Converter1<MultiplayerGameResultDto, MultiplayerGameResultDetailsDto, AllGameMovesDto> {

  override fun convert(from: MultiplayerGameResultDto, param1: AllGameMovesDto) = MultiplayerGameResultDetailsDto(
      gameId = from.gameId,
      status = from.status,
      firstPlayerMark = from.firstPlayerMark,
      secondPlayerMark = from.secondPlayerMark,
      startDate = from.startDate,
      endDate = from.endDate,
      moves = param1.moves
  )
}