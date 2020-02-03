package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.mapper.RegisterMapper
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.LoginRequestDto
import com.piotr.tictactoe.user.dto.LoginResponseDto
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import com.piotr.tictactoe.user.exception.UserNotExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
class UserFacade {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var registerMapper: RegisterMapper

  fun register(dto: RegisterRequestDto): RegisterResponseDto {
    checkIfEmailIsAlreadyRegistered(dto)
    checkIfPasswordsAreTheSame(dto)
    return userRepository.save(registerMapper.fromRequest(dto))
        .let(registerMapper::toResponse)
  }

  fun login(dto: LoginRequestDto): LoginResponseDto {
    val user = finsUserByEmail(dto.email)
    return LoginResponseDto(
        token = "fsdg" // TODO
    )
  }

  fun getLoggedUser(): UserDto {
    val email = getAuthenticatedUserEmail()
    return finsUserByEmail(email).let(::mapUserToDto)
  }

  private fun finsUserByEmail(email: String): User =
      userRepository.findUserByEmail(email) ?: throw UserNotExistsException()

  private fun checkIfEmailIsAlreadyRegistered(dto: RegisterRequestDto) {
    if (userRepository.findUserByEmail(dto.email) != null) {
      throw EmailAlreadyExistsException()
    }
  }

  private fun checkIfPasswordsAreTheSame(dto: RegisterRequestDto) {
    if (dto.password != dto.repeatedPassword) {
      throw PasswordsAreDifferentException()
    }
  }

  private fun mapUserToDto(user: User) = UserDto(
      id = user.id!!,
      email = user.email,
      username = user.username
  )

  private fun getAuthenticatedUserEmail(): String = SecurityContextHolder.getContext().authentication.name

  fun createUserFacade() = UserFacade()
}