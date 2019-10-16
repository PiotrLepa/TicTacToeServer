package com.piotr.tictactoe.rest

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("api/game/")
class GameController {

    @GetMapping
    fun home(): String {
        return "Works"
    }

    @PostMapping("createGame")
    fun createGame() {

    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GameController::class.java)
    }
}