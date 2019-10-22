package com.piotr.tictactoe.domain.exeptions.handlers

import com.piotr.tictactoe.domain.exeptions.GameEndedException
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
          .body(ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Game has ended"))
}