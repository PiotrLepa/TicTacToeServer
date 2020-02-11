package com.piotr.tictactoe.logic.exception

data class ErrorResponse(
  val code: Int,
  val exception: String,
  val message: String? = null
) {

  constructor(code: Int, exception: Exception) : this(code, exception::class.simpleName.toString(), exception.message)
}