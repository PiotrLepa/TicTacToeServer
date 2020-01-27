package com.piotr.tictactoe.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.anonymous().disable()
        .requestMatchers()
        .antMatchers("/api/**")
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
  }
}