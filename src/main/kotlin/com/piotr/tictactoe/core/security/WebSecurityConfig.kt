package com.piotr.tictactoe.core.security

import com.piotr.tictactoe.core.security.error.OAuth2ExceptionEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var authUserDetailsService: UserDetailsService

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  @Autowired
  private lateinit var oAuth2ExceptionEntryPoint: OAuth2ExceptionEntryPoint

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(authUserDetailsService)
        .passwordEncoder(passwordEncoder)
  }

  override fun configure(web: WebSecurity) {
    web.ignoring()
        .antMatchers("/user/register")
        .antMatchers("/multiplayer-game-socket")
  }

  override fun configure(http: HttpSecurity) {
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// TODO needed?
        .and()
        .csrf().disable()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .exceptionHandling().authenticationEntryPoint(oAuth2ExceptionEntryPoint)
  }

  @Bean
  override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
}