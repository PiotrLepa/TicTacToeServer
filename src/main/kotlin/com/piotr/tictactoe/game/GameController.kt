package com.piotr.tictactoe.game

import com.piotr.tictactoe.game.domain.GameFacade
import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameController {

  @Autowired
  private lateinit var gameFacade: GameFacade

  @PostMapping("/create")
  fun createGame(@RequestParam("difficulty_level") difficultyLevel: DifficultyLevel): ResponseEntity<GameWithComputerDto> {
    val game = gameFacade.createGameWithComputer(difficultyLevel)
    return ResponseEntity(game, HttpStatus.CREATED)
  }

//  @PutMapping("/field")
//  fun setField(@RequestBody playerMove: PlayerMoveDto): ResponseEntity<GameDto> {
//    val game = gameFacade.setField(playerMove)
//    return ResponseEntity(game, HttpStatus.OK)
//  }
//
//  @PostMapping("/resetBoard")
//  fun resetBoard(@RequestBody resetBoard: ResetBoardDto): ResponseEntity<GameDto> {
//    val game = gameFacade.resetBoard(resetBoard)
//    return ResponseEntity(game, HttpStatus.OK)
//  }
}