package com.piotr.tictactoe.logic

import com.piotr.tictactoe.game.exception.GameEndedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GameEndedExceptionHandlerAdvice {

  @ExceptionHandler(GameEndedException::class)
  fun handleException(exception: GameEndedException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  private fun createErrorResponse(status: HttpStatus, exception: Exception): ResponseEntity<ErrorResponse> =
      ResponseEntity
          .status(status)
          .body(ErrorResponse(status.value(), exception))
}