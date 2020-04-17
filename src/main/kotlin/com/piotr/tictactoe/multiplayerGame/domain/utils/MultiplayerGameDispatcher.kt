package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class MultiplayerGameDispatcher {

  @Autowired
  private lateinit var messagingTemplate: SimpMessageSendingOperations

  @Async
  fun initGame(gameDto: MultiplayerGameDto) {
    Thread.sleep(5000) // give clients some time to connect to game status web socket
    dispatchUpdate(gameDto)
  }

  fun updateGameStatus(gameDto: MultiplayerGameDto) {
    dispatchUpdate(gameDto)
  }

  private fun dispatchUpdate(gameDto: MultiplayerGameDto) {
    messagingTemplate.convertAndSend("/game-status/12", gameDto) // TODO dynamic game id
  }
}