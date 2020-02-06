package com.piotr.tictactoe.user;

import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserDto
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
  fun register(@RequestBody dto: RegisterDto): ResponseEntity<UserDto> =
      ResponseEntity(userFacade.register(dto), HttpStatus.CREATED)
}