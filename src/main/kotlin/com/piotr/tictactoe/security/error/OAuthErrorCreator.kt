package com.piotr.tictactoe.security.error

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OAuthErrorCreator {

  @Bean
  fun createOauthExceptionTranslator() = OAuthResponseExceptionTranslator()

  @Bean
  fun createOauthExceptionEntryPoint() = OAuthExceptionEntryPoint()
}