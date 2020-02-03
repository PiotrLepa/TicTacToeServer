package com.piotr.tictactoe.logic

import com.piotr.tictactoe.game.domain.GameEndedException
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import com.piotr.tictactoe.user.exception.UserNotExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GameEndedExceptionHandlerAdvice {

  @ExceptionHandler(EmailAlreadyExistsException::class)
  fun handleEmailAlreadyExists(exception: EmailAlreadyExistsException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(PasswordsAreDifferentException::class)
  fun handlePasswordsAreDifferent(exception: PasswordsAreDifferentException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(UserNotExistsException::class)
  fun handleUserNotExistsException(exception: UserNotExistsException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(GameEndedException::class)
  fun handleGameEndedException(exception: GameEndedException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  private fun createErrorResponse(status: HttpStatus, exception: Exception): ResponseEntity<ErrorResponse> =
      ResponseEntity
          .status(status)
          .body(ErrorResponse(status.value(), exception::class.simpleName.toString(), exception.message))
}