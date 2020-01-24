package com.piotr.tictactoe.security.auth

data class JwtRequest(
  val username: String,
  val password: String
)