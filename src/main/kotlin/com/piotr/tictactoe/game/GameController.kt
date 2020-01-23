package com.piotr.tictactoe.game

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameController {

//  @Autowired
//  private lateinit var gameFacade: GameFacade
//
//  @PostMapping("/create")
//  fun createGame(): ResponseEntity<GameDto> {
//    val game = gameFacade.createGame()
//    return ResponseEntity(game, HttpStatus.CREATED)
//  }
//
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