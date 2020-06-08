package com.piotr.tictactoe.user.dto

data class UserDto(
  val id: Long,
  val email: String,
  val username: String,
  val deviceToken: String,
  val playerCode: String,
  val languageTag: String
)