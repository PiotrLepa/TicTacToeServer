package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service

@Service
class MultiplayerGameFacade {

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var messagingTemplate: SimpMessageSendingOperations

  fun createMultiplayerGame(opponentCode: String) {
    val firstPlayer = userFacade.getLoggedUser()
//    val secondPlayer = userFacade.getUserByPlayerCode(opponentCode)
    messagingTemplate.convertAndSend("/game-status", firstPlayer)
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int) {
  }
}