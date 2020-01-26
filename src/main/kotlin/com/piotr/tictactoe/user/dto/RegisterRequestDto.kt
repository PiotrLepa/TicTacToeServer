package com.piotr.tictactoe.user.dto

data class RegisterRequestDto(
  val email: String,
  val username: String,
  val password: String,
  val repeatedPassword: String
)