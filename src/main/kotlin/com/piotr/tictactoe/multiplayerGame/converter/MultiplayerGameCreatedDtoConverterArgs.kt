package com.piotr.tictactoe.multiplayerGame.converter

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.multiplayerGame.dto.PlayerType

data class MultiplayerGameCreatedDtoConverterArgs(
  val yourMark: FieldMark,
  val playerType: PlayerType
)