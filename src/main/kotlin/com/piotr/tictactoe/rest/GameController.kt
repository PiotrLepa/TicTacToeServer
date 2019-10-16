package com.piotr.tictactoe.rest

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController("api/game/")
class GameController {

  @GetMapping
  fun home(): String {
    return "Works"
  }

  @PostMapping("queue")
  fun joinToQueue() {

  }

  @DeleteMapping("queue")
  fun removeFromQueue() {

  }

  @PostMapping("create")
  fun createGame() {

  }

  @PutMapping("{gameId}/field")
  fun setField(@PathVariable("gameId") gameId: String) {

  }

  @PutMapping("{gameId}/reset")
  fun resetGame(@PathVariable("gameId") gameId: String) {

  }

  companion object {
    private val LOGGER = LoggerFactory.getLogger(GameController::class.java)
  }
}