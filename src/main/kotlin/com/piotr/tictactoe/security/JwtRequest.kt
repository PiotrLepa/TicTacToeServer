package com.piotr.tictactoe.security

import java.io.Serializable

data class JwtRequest(
  val username: String,
  val password: String
) : Serializable