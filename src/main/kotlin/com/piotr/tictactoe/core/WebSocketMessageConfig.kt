package com.piotr.tictactoe.core

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketMessageConfig : WebSocketMessageBrokerConfigurer {

  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.addEndpoint("/multiplayer-game-socket")
        .setAllowedOrigins("*")
  }
}