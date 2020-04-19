package com.piotr.tictactoe.core.exception

import com.piotr.tictactoe.gameMove.domain.FieldAlreadyTakenException
import com.piotr.tictactoe.gameMove.domain.InvalidFieldIndexRangeException
import com.piotr.tictactoe.gameResult.exception.GameIsOnGoingException
import com.piotr.tictactoe.multiplayerGame.exception.GameAlreadyStaredException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidPlayerException
import com.piotr.tictactoe.multiplayerGame.exception.OpponentMoveException
import com.piotr.tictactoe.singlePlayerGame.exception.GameEndedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordTooShortException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import com.piotr.tictactoe.user.exception.UsernameAlreadyExistsException
import com.piotr.tictactoe.user.exception.UsernameTooShortException
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

  @ExceptionHandler(InvalidOpponentCodeException::class)
  fun handleInvalidOpponentCodeException(exception: InvalidOpponentCodeException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "multiplayer-game.error.invalid_opponent_code")

  @ExceptionHandler(InvalidPlayerException::class)
  fun handleInvalidPlayerException(exception: InvalidPlayerException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(GameAlreadyStaredException::class)
  fun handleGameAlreadyStaredException(exception: GameAlreadyStaredException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(OpponentMoveException::class)
  fun handleOpponentMoveException(exception: OpponentMoveException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

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
  fun handleInvalidFieldIndexRangeException(exception: InvalidFieldIndexRangeException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception)

  @ExceptionHandler(EmailAlreadyExistsException::class)
  fun handleEmailAlreadyExistsException(exception: EmailAlreadyExistsException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "user.error.email_already_exists")

  @ExceptionHandler(UsernameAlreadyExistsException::class)
  fun handleUsernameAlreadyExistsException(exception: UsernameAlreadyExistsException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "user.error.username_already_exists")

  @ExceptionHandler(PasswordTooShortException::class)
  fun handlePasswordTooShortException(exception: PasswordTooShortException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "user.error.password_too_short")

  @ExceptionHandler(UsernameTooShortException::class)
  fun handleUsernameTooShortException(exception: UsernameTooShortException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "user.error.username_too_short")

  @ExceptionHandler(PasswordsAreDifferentException::class)
  fun handlePasswordsAreDifferentException(exception: PasswordsAreDifferentException) =
      createErrorResponse(HttpStatus.BAD_REQUEST, exception, "user.error.passwords_are_different")

  private fun createErrorResponse(
    status: HttpStatus,
    exception: Exception,
    messageKey: String? = null
  ): ResponseEntity<ErrorResponse> =
      ResponseEntity
          .status(status)
          .body(ErrorResponse(
              code = status.value(),
              exception = exception,
              printableMessage = messageKey?.let { getMessage(it) }
          ))

  private fun getMessage(code: String, args: Array<Any>? = null): String =
      messageSource.getMessage(code, args, LocaleContextHolder.getLocale())
}