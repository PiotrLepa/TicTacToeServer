package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
class MultiplayerGameDispatcher {

  @Autowired
  private lateinit var messagingTemplate: SimpMessageSendingOperations

  fun updateGameStatus(gameDto: MultiplayerGameDto) {
    messagingTemplate.convertAndSend(GAME_STATUS_URL + "12", gameDto) // TODO dynamic game id
  }

  companion object {
    const val GAME_STATUS_URL = "/game-status/"
  }
}