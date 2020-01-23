package com.piotr.tictactoe.security

data class JwtRequest(
  val username: String,
  val password: String
)