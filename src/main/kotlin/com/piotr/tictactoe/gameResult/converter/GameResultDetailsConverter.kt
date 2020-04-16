package com.piotr.tictactoe.gameResult.converter

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.GameResultDetailsDto
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import org.springframework.stereotype.Component

@Component
class GameResultDetailsConverter : Converter1<SinglePlayerGameDetailsDto, GameResultDetailsDto, AllGameMovesDto> {

  override fun convert(from: SinglePlayerGameDetailsDto, param1: AllGameMovesDto) = GameResultDetailsDto(
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