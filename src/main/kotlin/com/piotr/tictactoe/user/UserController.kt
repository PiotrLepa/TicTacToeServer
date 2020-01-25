package com.piotr.tictactoe.user;

import com.piotr.tictactoe.security.AuthUserDetailsService
import com.piotr.tictactoe.security.JwtTokenUtil
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class UserController {

  @Autowired
  private lateinit var authenticationManager: AuthenticationManager

  @Autowired
  private lateinit var jwtTokenUtil: JwtTokenUtil

  @Autowired
  private lateinit var userDetailsService: AuthUserDetailsService

  @PostMapping("/authenticate")
  fun createAuthenticationToken(@RequestBody authenticationDto: RegisterDto): ResponseEntity<*> {
    authenticate(authenticationDto.login, authenticationDto.password)
    val userDetails = userDetailsService.loadUserByUsername(authenticationDto.login)
    return if (userDetails != null) {
      val token = jwtTokenUtil.generateToken(userDetails)
      ResponseEntity.ok(UserDto(token))
    } else {
      ResponseEntity.badRequest().body("User not found")
    }
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