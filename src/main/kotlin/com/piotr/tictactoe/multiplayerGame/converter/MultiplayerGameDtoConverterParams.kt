package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.gameMove.dto.GameMoveDto

data class MultiplayerGameDtoConverterParams(
  val firstPlayerCode: String,
  val secondPlayerCode: String,
  val moves: List<GameMoveDto>
)