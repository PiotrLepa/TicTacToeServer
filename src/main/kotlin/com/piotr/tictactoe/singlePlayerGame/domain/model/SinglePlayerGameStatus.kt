package com.piotr.tictactoe.singlePlayerGame.domain.model

enum class SinglePlayerGameStatus {
  ON_GOING,
  PLAYER_WON,
  COMPUTER_WON,
  DRAW;

  companion object {
    fun getEndedGameStatus(): List<SinglePlayerGameStatus> = listOf(
        PLAYER_WON,
        COMPUTER_WON,
        DRAW
    )
  }
}