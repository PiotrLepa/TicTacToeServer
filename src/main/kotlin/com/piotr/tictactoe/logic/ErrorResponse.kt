package com.piotr.tictactoe.logic

data class ErrorResponse(
  val code: Int,
  val exception: Exception
)