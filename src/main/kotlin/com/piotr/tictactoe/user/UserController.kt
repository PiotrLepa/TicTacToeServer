package com.piotr.tictactoe.user;

import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.LoginRequestDto
import com.piotr.tictactoe.user.dto.LoginResponseDto
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user/")
class UserController {

  @Autowired
  private lateinit var userFacade: UserFacade

  @PostMapping("/register")
  fun register(@RequestBody dto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> =
      ResponseEntity(userFacade.register(dto), HttpStatus.CREATED)

  @PostMapping("/login")
  fun login(@RequestBody dto: LoginRequestDto): ResponseEntity<LoginResponseDto> =
      ResponseEntity.ok(userFacade.login(dto))

  @GetMapping("/test")
  fun login(): ResponseEntity<UserDto> {
    return ResponseEntity.ok(userFacade.getLoggedUser())
  }
}