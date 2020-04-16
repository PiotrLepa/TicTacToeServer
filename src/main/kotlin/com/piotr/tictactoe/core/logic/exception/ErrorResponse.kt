package com.piotr.tictactoe.core.logic.exception

data class ErrorResponse(
  val code: Int,
  val developerMessage: String,
  val printableMessage: String? = null
) {

  constructor(code: Int, exception: Exception, printableMessage: String? = null)
      : this(code, exception::class.simpleName.toString(), printableMessage)
}