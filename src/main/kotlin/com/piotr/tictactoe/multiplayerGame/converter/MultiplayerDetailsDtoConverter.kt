package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import org.springframework.stereotype.Component

@Component
class MultiplayerDetailsDtoConverter : Converter<MultiplayerGame, MultiplayerGameResultDto> {

  override fun convert(from: MultiplayerGame) = MultiplayerGameResultDto(
      gameId = from.gameId!!,
      status = from.status,
      firstPlayerMark = from.firstPlayerMark,
      secondPlayerMark = from.secondPlayerMark,
      startDate = from.creationDate,
      endDate = from.modificationDate
  )
}