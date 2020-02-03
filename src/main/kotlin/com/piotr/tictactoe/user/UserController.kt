package com.piotr.tictactoe.user;

import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.LoginRequestDto
import com.piotr.tictactoe.user.dto.LoginResponseDto
import com.piotr.tictactoe.user.dto.RegisterRequestDto
import com.piotr.tictactoe.user.dto.RegisterResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

  @Autowired
  private lateinit var userFacade: UserFacade

  @PostMapping("/register")
  fun register(@RequestBody dto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> =
      ResponseEntity(userFacade.register(dto), HttpStatus.CREATED)

  @PostMapping("/login")
  fun login(@RequestBody dto: LoginRequestDto): ResponseEntity<LoginResponseDto> =
      ResponseEntity.ok(userFacade.login(dto))
}