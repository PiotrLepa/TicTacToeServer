package com.piotr.tictactoe.core.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Oath2Properties @Autowired constructor(
  @Value("\${oauth2.client_id}")
  val clientId: String,

  @Value("\${oauth2.client_secret}")
  val clientSecret: String
)