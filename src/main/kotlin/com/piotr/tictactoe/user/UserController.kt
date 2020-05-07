package com.piotr.tictactoe.user;

import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserProfileDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
  private val userFacade: UserFacade
) {

  @PostMapping("/register")
  fun register(@RequestBody dto: RegisterDto): ResponseEntity<UserProfileDto> =
      ResponseEntity(userFacade.register(dto), HttpStatus.CREATED)

  @GetMapping("/profile")
  fun getUserProfile(): ResponseEntity<UserProfileDto> =
      ResponseEntity(userFacade.getUserProfile(), HttpStatus.OK)
}