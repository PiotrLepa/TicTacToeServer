package com.piotr.tictactoe.core.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class OathProperties {

  @Value("\${oauth2.client_id}")
  lateinit var clientId: String

  @Value("\${oauth2.client_secret}")
  lateinit var clientSecret: String
}