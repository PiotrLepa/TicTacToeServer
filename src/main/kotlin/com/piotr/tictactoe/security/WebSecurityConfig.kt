package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var authUserDetailsService: UserDetailsService

  @Autowired
  private lateinit var authRequestFilter: AuthRequestFilter

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  @Autowired
  private lateinit var authEntryPoint: AuthEntryPoint

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    auth
        .userDetailsService(authUserDetailsService)
        .passwordEncoder(passwordEncoder) // TODO needed?
  }

//  override fun configure(http: HttpSecurity) {
//    http
//        .authorizeRequests()
//        .antMatchers("/oauth/token").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .httpBasic()
//        .and()
//        .csrf().disable()
//  }

  @Bean
  override fun authenticationManagerBean(): AuthenticationManager? {
    return super.authenticationManagerBean()
  }

//  @Autowired
//  fun configureGlobal(auth: AuthenticationManagerBuilder) {
//    auth.userDetailsService(authUserDetailsService).passwordEncoder(passwordEncoder)
//  }
//
//  @Bean
//  override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
//
//  override fun configure(httpSecurity: HttpSecurity) {
//    httpSecurity.csrf().disable()
//        .addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
//        .authorizeRequests()
//        .antMatchers("/user/register", "/user/login").permitAll() // don't authenticate this particular request
//        .anyRequest().authenticated()// all other requests need to be authenticated
//        .and()
//        .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session won't be used to store user's state.
//  }
}