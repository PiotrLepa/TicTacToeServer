package com.piotr.tictactoe.game

import com.piotr.tictactoe.game.domain.GameFacade
import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.dto.GameResultDetailsDto
import com.piotr.tictactoe.game.dto.GameResultDto
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

  @PutMapping("/{gameId}/move/{fieldIndex}")
  fun setMove(
    @PathVariable("gameId") gameId: Long,
    @PathVariable("fieldIndex") fieldIndex: Int
  ): ResponseEntity<GameWithComputerDto> {
    val move = gameFacade.setPlayerMoveAndGetComputerMove(gameId, fieldIndex)
    return ResponseEntity(move, HttpStatus.OK)
  }

  @GetMapping("/results")
  fun getResults(): ResponseEntity<List<GameResultDto>> {
    val results = gameFacade.getGameResults()
    return ResponseEntity(results, HttpStatus.OK)
  }

  @GetMapping("/results/{gameId}")
  fun getResultDetails(@PathVariable("gameId") gameId: Long): ResponseEntity<GameResultDetailsDto> {
    val resultDetails = gameFacade.getGameResultDetails(gameId)
    return ResponseEntity(resultDetails, HttpStatus.OK)
  }
}