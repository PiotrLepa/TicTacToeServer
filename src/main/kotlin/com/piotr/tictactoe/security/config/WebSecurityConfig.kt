package com.piotr.tictactoe.security.config

import com.piotr.tictactoe.security.error.OAuthExceptionEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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
  private lateinit var oAuthExceptionEntryPoint: OAuthExceptionEntryPoint

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(authUserDetailsService)
        .passwordEncoder(passwordEncoder)
  }

  override fun configure(http: HttpSecurity) {
    http
        .sessionManagement() // TODO needed?
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/oauth/token").permitAll()
        .antMatchers("/register").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .exceptionHandling().authenticationEntryPoint(oAuthExceptionEntryPoint)
  }

  @Bean
  override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
}