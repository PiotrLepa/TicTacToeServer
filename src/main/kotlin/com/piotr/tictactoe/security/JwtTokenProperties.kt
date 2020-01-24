package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtTokenProperties {

  @Value("\${jwt.secret_key}")
  lateinit var secretKey: String

  @Value("\${jwt.token_prefix}")
  lateinit var token_prefix: String

  @Value("\${jwt.token_expiration_minutes}")
  lateinit var tokenExpirationMinutes: String
}