package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
class MultiplayerGameDispatcher @Autowired constructor(
  private val messagingTemplate: SimpMessageSendingOperations
) {

  fun updateGameStatus(gameDto: MultiplayerGameDto) {
    messagingTemplate.convertAndSend(GAME_STATUS_URL + gameDto.socketDestination, gameDto)
  }

  companion object {
    const val GAME_STATUS_URL = "/game-status/"
  }
}