package com.piotr.tictactoe.logic

import com.piotr.tictactoe.game.exception.GameEndedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GameEndedExceptionHandlerAdvice {

  @ExceptionHandler(GameEndedException::class)
  fun handleException(gameEndedException: GameEndedException) =
      ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse(
              HttpStatus.BAD_REQUEST.value(), "Game has ended"))
}