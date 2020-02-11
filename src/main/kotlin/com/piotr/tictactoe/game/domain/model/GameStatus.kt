package com.piotr.tictactoe.game.domain.model

enum class GameStatus {
  ON_GOING,
  PLAYER_WON,
  COMPUTER_WON,
  DRAW;

  companion object {
    fun getEndedGameStatus(): List<GameStatus> = listOf(PLAYER_WON, COMPUTER_WON, DRAW)
  }
}