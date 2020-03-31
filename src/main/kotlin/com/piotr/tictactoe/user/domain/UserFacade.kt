package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordTooShortException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserFacade {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  fun register(dto: RegisterDto): UserDto {
    checkIfEmailIsAlreadyRegistered(dto)
    checkPasswordLength(dto)
    checkIfPasswordsAreTheSame(dto)
    return userRepository.save(mapUserFromRegisterDto(dto)).toDto()
  }

  fun getLoggedUser(): UserDto {
    val email = getAuthenticatedUserEmail()
    return findUserByEmail(email)!!
  }

  private fun findUserByEmail(email: String): UserDto? =
      userRepository.findUserByEmail(email)?.toDto()

  private fun checkIfEmailIsAlreadyRegistered(dto: RegisterDto) {
    if (userRepository.findUserByEmail(dto.email) != null) {
      throw EmailAlreadyExistsException()
    }
  }

  private fun checkPasswordLength(dto: RegisterDto) {
    if (dto.password.length < PASSWORD_MIN_LENGTH) {
      throw PasswordTooShortException()
    }
  }

  private fun checkIfPasswordsAreTheSame(dto: RegisterDto) {
    if (dto.password != dto.repeatedPassword) {
      throw PasswordsAreDifferentException()
    }
  }

  private fun getAuthenticatedUserEmail(): String = SecurityContextHolder.getContext().authentication.name

  private fun mapUserFromRegisterDto(dto: RegisterDto) = User(
      email = dto.email,
      username = dto.username,
      password = passwordEncoder.encode(dto.password)
  )

  companion object {
    private val PASSWORD_MIN_LENGTH = 6
  }
}