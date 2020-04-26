package com.piotr.tictactoe.user.converter

data class RegisterEntityConverterArgs(
  val languageTag: String,
  val deviceToken: String,
  val playerCode: String
)