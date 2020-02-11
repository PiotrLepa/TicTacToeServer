package com.piotr.tictactoe.security.error

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OAuth2ErrorCreator {

  @Bean
  fun createOauthExceptionTranslator() = OAuth2ResponseExceptionTranslator()

  @Bean
  fun createOauthExceptionEntryPoint() = OAuth2ExceptionEntryPoint()
}