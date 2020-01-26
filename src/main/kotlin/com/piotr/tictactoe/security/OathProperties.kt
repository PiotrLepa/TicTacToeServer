package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class OathProperties {

  @Value("\${jwt.secret_key}")
  lateinit var secretKey: String

  @Value("\${jwt.token_prefix}")
  lateinit var tokenPrefix: String

  @Value("\${jwt.token_expiration_minutes}")
  var tokenExpirationMinutes: Int = 0
}