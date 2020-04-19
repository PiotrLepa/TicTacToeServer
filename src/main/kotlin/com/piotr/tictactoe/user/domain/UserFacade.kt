package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordTooShortException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import com.piotr.tictactoe.user.exception.UsernameAlreadyExistsException
import com.piotr.tictactoe.user.exception.UsernameTooShortException
import com.piotr.tictactoe.utils.PlayerCodeGenerator
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

  @Autowired
  private lateinit var playerCodeGenerator: PlayerCodeGenerator

  @Autowired
  private lateinit var userDtoConverter: Converter<User, UserDto>

  fun register(dto: RegisterDto): UserDto {
    checkUsernameLength(dto)
    checkPasswordLength(dto)
    checkIfPasswordsAreTheSame(dto)
    checkIfEmailIsUnique(dto)
    checkIfUsernameIsUnique(dto)
    val playerCode = getUniquePlayerCode()
    return userRepository.save(mapUserFromRegisterDto(dto, playerCode)).let(userDtoConverter::convert)
  }

  fun getLoggedUser(): UserDto {
    val email = getAuthenticatedUserEmail()
    return findUserByEmail(email)!!
  }

  fun findUserByPlayerCode(code: String): UserDto? =
      userRepository.findUserByPlayerCode(code)?.let(userDtoConverter::convert)

  fun findUserById(id: Long): UserDto =
      userRepository.findById(id).get().let(userDtoConverter::convert)

  private fun findUserByEmail(email: String): UserDto? =
      userRepository.findUserByEmail(email)?.let(userDtoConverter::convert)

  private fun getAuthenticatedUserEmail(): String = SecurityContextHolder.getContext().authentication.name

  private fun getUniquePlayerCode(): String {
    val playerCode = playerCodeGenerator.generate()
    return if (userRepository.findUserByPlayerCode(playerCode) == null) {
      playerCode
    } else {
      getUniquePlayerCode()
    }
  }

  private fun checkUsernameLength(dto: RegisterDto) {
    if (dto.username.length < USERNAME_MIN_LENGTH) {
      throw UsernameTooShortException()
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

  private fun checkIfEmailIsUnique(dto: RegisterDto) {
    if (userRepository.findUserByEmail(dto.email) != null) {
      throw EmailAlreadyExistsException()
    }
  }

  private fun checkIfUsernameIsUnique(dto: RegisterDto) {
    if (userRepository.findUserByUsername(dto.username) != null) {
      throw UsernameAlreadyExistsException()
    }
  }

  private fun mapUserFromRegisterDto(dto: RegisterDto, playerCode: String) = User(
      email = dto.email,
      username = dto.username,
      password = passwordEncoder.encode(dto.password),
      deviceToken = "",
      playerCode = playerCode
  )

  companion object {
    private const val PASSWORD_MIN_LENGTH = 6
    private const val USERNAME_MIN_LENGTH = 3
  }
}