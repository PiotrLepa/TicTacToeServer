package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.mapper.RegisterMapper
import com.piotr.tictactoe.security.AuthUserDetailsService
import com.piotr.tictactoe.security.JwtTokenUtil
import com.piotr.tictactoe.user.dto.LoginRequestDto
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.PasswordsAreDifferentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

@Configuration
class UserFacade {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var authenticationManager: AuthenticationManager

  @Autowired
  private lateinit var jwtTokenUtil: JwtTokenUtil

  @Autowired
  private lateinit var userDetailsService: AuthUserDetailsService

  @Autowired
  private lateinit var registerMapper: RegisterMapper

  fun register(dto: RegisterRequestDto): RegisterResponseDto {
    checkIfEmailIsAlreadyRegistered(dto)
    checkIfPasswordsAreTheSame(dto)
    return userRepository.save(registerMapper.fromRequest(dto))
        .let(registerMapper::toResponse)
  }

  fun login(dto: LoginRequestDto) {

  }

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

  private fun authenticate(username: String, password: String) {
    authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
  }

//  fun mapRegisterDtoToUser(dto: RegisterRequestDto) =
//      User(
//          email = dto.email,
//          username = dto.username,
//          password = passwordEncoder.encode(dto.password)
//      )

  fun create() = UserFacade()
}