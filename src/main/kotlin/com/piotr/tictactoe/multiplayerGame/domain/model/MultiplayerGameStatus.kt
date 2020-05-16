package com.piotr.tictactoe.multiplayerGame.domain.model

enum class MultiplayerGameStatus {
  CREATED,
  ON_GOING,
  FIRST_PLAYER_WON,
  SECOND_PLAYER_WON,
  DRAW,
  PLAYER_LEFT_GAME;

  companion object {
    fun getFinishedGameStatus(): List<MultiplayerGameStatus> = listOf(
        FIRST_PLAYER_WON,
        SECOND_PLAYER_WON,
        DRAW
    )
  }
}