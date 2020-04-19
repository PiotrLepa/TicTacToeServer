package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.multiplayerGame.domain.MultiplayerGameFacade
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher.Companion.GAME_STATUS_URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import java.util.concurrent.ConcurrentHashMap

@Service
class MultiplayerGameSocketListener @Autowired constructor(
  private val multiplayerGameFacade: MultiplayerGameFacade
) {

  private val sessionsWithDestinations = ConcurrentHashMap<String, String>()

  @EventListener
  fun handleSessionSubscribeEvent(event: SessionSubscribeEvent) {
    val headers = event.message.headers
    val sessionId = headers["simpSessionId"] as String
    val destination = headers["simpDestination"] as String
    sessionsWithDestinations[sessionId] = destination
  }

  @EventListener
  fun handleSessionDisconnectEvent(event: SessionDisconnectEvent) {
    val headers = event.message.headers
    val sessionId = headers["simpSessionId"] as String
    val destination = sessionsWithDestinations[sessionId] ?: return
    sessionsWithDestinations.remove(sessionId)
    val gameId = destination.removePrefix(GAME_STATUS_URL).toLong()
    multiplayerGameFacade.setPlayerLeftFromGame(gameId)
  }
}