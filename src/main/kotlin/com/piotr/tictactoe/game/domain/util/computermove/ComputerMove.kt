package com.piotr.tictactoe.game.domain.util.computermove

import com.piotr.tictactoe.game.domain.model.GameStatus

data class ComputerMove(
  val status: GameStatus,
  val fieldIndex: Int
)