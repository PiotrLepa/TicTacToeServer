package com.piotr.tictactoe.game

import com.piotr.tictactoe.game.domain.GameFacade
import com.piotr.tictactoe.game.dto.GameDto
import com.piotr.tictactoe.game.dto.PlayerMoveDto
import com.piotr.tictactoe.game.dto.ResetBoardDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameController {

  @Autowired
  private lateinit var gameFacade: GameFacade

  @PostMapping("/create")
  fun createGame(): ResponseEntity<GameDto> {
    val game = gameFacade.createGame()
    return ResponseEntity(game, HttpStatus.CREATED)
  }

  @PutMapping("/field")
  fun setField(@RequestBody playerMove: PlayerMoveDto): ResponseEntity<GameDto> {
    val game = gameFacade.setField(playerMove)
    return ResponseEntity(game, HttpStatus.OK)
  }

  @PostMapping("/resetBoard")
  fun resetBoard(@RequestBody resetBoard: ResetBoardDto): ResponseEntity<GameDto> {
    val game = gameFacade.resetBoard(resetBoard)
    return ResponseEntity(game, HttpStatus.OK)
  }
}