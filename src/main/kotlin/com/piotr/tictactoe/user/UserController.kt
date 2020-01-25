package com.piotr.tictactoe.user;

import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class UserController {

  @Autowired
  private lateinit var userFacade: UserFacade

  @PostMapping("/register")
  fun register(@RequestBody dto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> =
      ResponseEntity(userFacade.register(dto), HttpStatus.CREATED)

//  @PostMapping("/login")
//  fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
//    authenticate(authenticationDto.login, authenticationDto.password)
//    val userDetails = userDetailsService.loadUserByUsername(authenticationDto.login)
//    return if (userDetails != null) {
//      val token = jwtTokenUtil.generateToken(userDetails)
//      ResponseEntity.ok(UserDto(token))
//    } else {
//      ResponseEntity.badRequest().body("User not found")
//    }
//    userFacade.login(reg)
//  }
}