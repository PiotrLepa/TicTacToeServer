package com.piotr.tictactoe.lobby

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping

class LobbyController {

  @PostMapping("queue")
  fun joinToLobby() {
  }

  @DeleteMapping("queue")
  fun removeFromLobby() {

  }
}