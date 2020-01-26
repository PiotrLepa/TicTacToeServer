package com.piotr.tictactoe.user

import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
  @GetMapping(value = ["/"]) fun index(): String {
    return "Hello world"
  }

  @GetMapping(value = ["/private"]) fun privateArea(): String {
    return "Private area"
  }
}