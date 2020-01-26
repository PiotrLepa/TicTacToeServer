package com.piotr.tictactoe.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

/**
 * The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically
 * to the Spring Security filter chain.
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

//  override fun configure(http: HttpSecurity) {
//    http
//        .headers()
//        .frameOptions()
//        .disable()
//        .and()
//        .authorizeRequests()
//        .antMatchers("/", "/home", "/user/register", "/user/login").permitAll()
//        .antMatchers("/private/**").authenticated()
//  }

  @Throws(Exception::class) override fun configure(http: HttpSecurity) {
    http.anonymous().disable()
        .authorizeRequests()
        .antMatchers("/", "/home", "/user/register", "/user/login").permitAll()
        .antMatchers("/private/**").authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
  }
}