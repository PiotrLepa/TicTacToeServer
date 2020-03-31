package com.piotr.tictactoe.logic.exception

import com.piotr.tictactoe.game.domain.GameEndedException
import com.piotr.tictactoe.game.domain.GameIsOnGoingException
import com.piotr.tictactoe.game.domain.WrongPlayerException
import com.piotr.tictactoe.move.domain.FieldAlreadyTakenException
import com.piotr.tictactoe.move.domain.InvalidFieldIndexRangeException
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GameEndedExceptionHandlerAdvice {

  @Autowired
  private lateinit var messageSource: MessageSource

  @ExceptionHandler(GameIsOnGoingException::class)
  fun handleGameIsOnGoingException(exception: GameIsOnGoingException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(FieldAlreadyTakenException::class)
  fun handleFieldAlreadyTakenException(exception: FieldAlreadyTakenException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(GameEndedException::class)
  fun handleGameEndedException(exception: GameEndedException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(WrongPlayerException::class)
  fun handleWrongPlayerException(exception: WrongPlayerException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(InvalidFieldIndexRangeException::class)
  fun handleInvalidFieldIndexRange(exception: InvalidFieldIndexRangeException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(EmailAlreadyExistsException::class)
  fun handleEmailAlreadyExists(exception: EmailAlreadyExistsException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, getMessage("user.error.email_already_exists"))

  @ExceptionHandler(PasswordsAreDifferentException::class)
  fun handlePasswordsAreDifferent(exception: PasswordsAreDifferentException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, getMessage("user.error.passwords_are_different"))

  private fun createErrorResponse(
    status: HttpStatus,
    exception: Exception,
    printableMessage: String? = null
  ): ResponseEntity<ErrorResponse> =
      ResponseEntity
          .status(status)
          .body(ErrorResponse(
              code = status.value(),
              exception = exception,
              printableMessage = printableMessage
          ))

  private fun getMessage(code: String, args: Array<Any>? = null): String =
      messageSource.getMessage(code, args, LocaleContextHolder.getLocale())
}