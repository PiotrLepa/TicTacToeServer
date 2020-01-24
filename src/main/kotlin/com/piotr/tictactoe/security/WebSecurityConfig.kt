package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var jwtUserDetailsService: UserDetailsService

  @Autowired
  private lateinit var jwtRequestFilter: JwtRequestFilter

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  @Autowired
  private lateinit var jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    // configure AuthenticationManager so that it knows from where
    // to load user for matching credentials
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder)
  }

  @Bean
  override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

  override fun configure(httpSecurity: HttpSecurity) {
    httpSecurity.csrf().disable()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java) // Add a filter to validate the tokens
        .authorizeRequests().antMatchers("/authenticate", "/register").permitAll() // don't authenticate this particular request
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // TODO needed?
        .anyRequest().authenticated()// all other requests need to be authenticated
        .and()
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session won't be used to store user's state.
  }
}