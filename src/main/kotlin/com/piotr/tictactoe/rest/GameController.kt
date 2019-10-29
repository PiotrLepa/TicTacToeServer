package com.piotr.tictactoe.rest

import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.PlayerMoveDto
import com.piotr.tictactoe.domain.dto.ResetBoardDto
import com.piotr.tictactoe.service.GameService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/game/")
class GameController {

  @Autowired
  private lateinit var gameService: GameService

  @PostMapping("queue")
  fun joinToQueue() {

  }

  @DeleteMapping("queue")
  fun removeFromQueue() {

  }

  @PostMapping("create")
  fun createGame(): ResponseEntity<GameDto> {
    val game = gameService.createGame()
    return ResponseEntity(game, HttpStatus.CREATED)
  }

  @PutMapping("field")
  fun setField(@RequestBody playerMove: PlayerMoveDto): ResponseEntity<GameDto> {
    val game = gameService.setField(playerMove)
    return ResponseEntity(game, HttpStatus.OK)
  }

  @PostMapping("resetBoard")
  fun resetBoard(@RequestBody resetBoard: ResetBoardDto): ResponseEntity<GameDto> {
    val game = gameService.resetBoard(resetBoard)
    return ResponseEntity(game, HttpStatus.OK)
  }

  companion object {
    private val LOGGER = LoggerFactory.getLogger(GameController::class.java)
  }
}