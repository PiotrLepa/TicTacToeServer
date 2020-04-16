package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameResult.dto.GameResultDetailsDto
import com.piotr.tictactoe.move.dto.AllMovesDto
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import org.springframework.stereotype.Component

@Component
class GameResultDetailsConverter : Converter1<SinglePlayerGameDetailsDto, GameResultDetailsDto, AllMovesDto> {

  override fun convert(from: SinglePlayerGameDetailsDto, param1: AllMovesDto) = GameResultDetailsDto(
      gameId = from.gameId,
      playerId = from.playerId,
      status = from.status,
      difficultyLevel = from.difficultyLevel,
      playerMark = from.playerMark,
      computerMark = from.computerMark,
      startDate = from.startDate,
      endDate = from.endDate,
      moves = param1.moves
  )
}