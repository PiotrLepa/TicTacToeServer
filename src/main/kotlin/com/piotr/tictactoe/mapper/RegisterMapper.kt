package com.piotr.tictactoe.mapper

import com.piotr.tictactoe.mapper.base.Mapper
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class RegisterMapper(
  private val passwordEncoder: PasswordEncoder
) : Mapper<RegisterRequestDto, RegisterResponseDto, User> {

  override fun fromRequest(request: RegisterRequestDto) = User(
      email = request.email,
      username = request.username,
      password = passwordEncoder.encode(request.password)
  )

  override fun toResponse(model: User) = RegisterResponseDto(
      email = model.email,
      username = model.username
  )

  @Bean
  @Autowired
  fun createRegisterMapper(passwordEncoder: PasswordEncoder) = RegisterMapper(passwordEncoder)
}