package com.piotr.tictactoe.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtProperties {

  @Value("\${jwt.secret_key}")
  lateinit var secretKey: String

  @Value("\${jwt.token_prefix}")
  lateinit var tokenPrefix: String

  @Value("\${jwt.token_expiration_minutes}")
  var tokenExpirationMinutes: Int = 0
}