package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.user.dto.RegisterDto
import org.springframework.context.annotation.Configuration

@Configuration
class UserFacade {

  fun save(registerDto: RegisterDto) {

  }

  fun create() = UserFacade()
}