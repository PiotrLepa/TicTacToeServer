package com.piotr.tictactoe.multiplayerGame.domain.model

enum class MultiplayerGameStatus {
  CREATED,
  ON_GOING,
  FIRST_PLAYER_WON,
  SECOND_PLAYER_WON,
  DRAW;

  companion object {
    fun getEndedGameStatus(): List<MultiplayerGameStatus> = listOf(
        FIRST_PLAYER_WON,
        SECOND_PLAYER_WON,
        DRAW
    )
  }
}