package com.piotr.tictactoe.user.dto

data class RegisterDto(
  val email: String,
  val login: String,
  val password: String,
  val repeatedPassword: String
)