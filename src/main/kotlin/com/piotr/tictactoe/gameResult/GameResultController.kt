package com.piotr.tictactoe.gameResult

import com.piotr.tictactoe.gameResult.domain.GameResultFacade
import com.piotr.tictactoe.gameResult.dto.GameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.GameResultDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game-result")
class GameResultController @Autowired constructor(
  private val gameResultFacade: GameResultFacade
) {

  @GetMapping("/user")
  fun getUserResults(pageable: Pageable): ResponseEntity<Page<GameResultDto>> {
    val pagedResults = gameResultFacade.getUserGameResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/all")
  fun getAllResults(pageable: Pageable): ResponseEntity<Page<GameResultDto>> {
    val pagedResults = gameResultFacade.getAllGameResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/{gameId}")
  fun getResultDetails(@PathVariable("gameId") gameId: Long): ResponseEntity<GameResultDetailsDto> {
    val resultDetails = gameResultFacade.getGameResultDetails(gameId)
    return ResponseEntity(resultDetails, HttpStatus.OK)
  }
}