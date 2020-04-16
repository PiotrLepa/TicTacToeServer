package com.piotr.tictactoe.singlePlayerGame

import com.piotr.tictactoe.singlePlayerGame.domain.SinglePlayerGameFacade
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/single-player-game")
class SinglePlayerGameController {

  @Autowired
  private lateinit var singlePlayerGameFacade: SinglePlayerGameFacade

  @PostMapping("/create")
  fun createSinglePlayerGame(@RequestParam("difficulty_level") difficultyLevel: DifficultyLevel): ResponseEntity<SinglePlayerGameDto> {
    val game = singlePlayerGameFacade.createSinglePlayerGame(difficultyLevel)
    return ResponseEntity(game, HttpStatus.CREATED)
  }

  @PutMapping("/{gameId}/move/{fieldIndex}")
  fun setMove(
    @PathVariable("gameId") gameId: Long,
    @PathVariable("fieldIndex") fieldIndex: Int
  ): ResponseEntity<SinglePlayerGameDto> {
    val move = singlePlayerGameFacade.setPlayerMoveAndGetComputerMove(gameId, fieldIndex)
    return ResponseEntity(move, HttpStatus.OK)
  }
}