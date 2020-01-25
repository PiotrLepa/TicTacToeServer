package com.piotr.tictactoe.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderCreator {

  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}