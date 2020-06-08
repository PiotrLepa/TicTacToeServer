package com.piotr.tictactoe.user.domain.utils

import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.exception.PasswordTooShortException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import com.piotr.tictactoe.user.exception.UsernameTooShortException
import org.springframework.stereotype.Component

@Component
class UserChecker {
  fun checkUsernameLength(dto: RegisterDto) {
    if (dto.username.length < USERNAME_MIN_LENGTH) {
      throw UsernameTooShortException()
    }
  }

  fun checkPasswordLength(dto: RegisterDto) {
    if (dto.password.length < PASSWORD_MIN_LENGTH) {
      throw PasswordTooShortException()
    }
  }

  fun checkIfPasswordsAreTheSame(dto: RegisterDto) {
    if (dto.password != dto.repeatedPassword) {
      throw PasswordsAreDifferentException()
    }
  }

  companion object {
    const val PASSWORD_MIN_LENGTH = 6
    const val USERNAME_MIN_LENGTH = 3
  }
} 