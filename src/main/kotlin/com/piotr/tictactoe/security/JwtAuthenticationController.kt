package com.piotr.tictactoe.security;

import com.piotr.tictactoe.user.UserDao
import com.piotr.tictactoe.user.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class JwtAuthenticationController {

  @Autowired
  private lateinit var authenticationManager: AuthenticationManager

  @Autowired
  private lateinit var jwtTokenUtil: JwtTokenUtil

  @Autowired
  private lateinit var userDetailsService: JwtUserDetailsService

  @PostMapping("/authenticate")
  fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<*> {
    authenticate(authenticationRequest.username, authenticationRequest.password)
    val userDetails: UserDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.username)
    val token = jwtTokenUtil.generateToken(userDetails)
    return ResponseEntity.ok(JwtResponse(token))
  }

  @PostMapping("/register")
  fun saveUser(@RequestBody user: UserDto): ResponseEntity<UserDao> {
    return ResponseEntity.ok(userDetailsService.save(user))
  }

  @GetMapping("/testget")
  fun testGet(): ResponseEntity<UserDto> {
    val user = UserDto("test", "pass")
    return ResponseEntity.ok(user)
  }

  private fun authenticate(username: String, password: String) {
    try {
      authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    } catch (e: DisabledException) {
      throw Exception("USER_DISABLED", e)
    } catch (e: BadCredentialsException) {
      throw Exception("INVALID_CREDENTIALS", e)
    }
  }
}