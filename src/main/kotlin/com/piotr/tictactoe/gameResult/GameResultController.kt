package com.piotr.tictactoe.gameResult

import com.piotr.tictactoe.gameResult.domain.GameResultFacade
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
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

  @GetMapping("/single-player/user")
  fun getSinglePlayerUserResults(pageable: Pageable): ResponseEntity<Page<SinglePlayerGameResultDto>> {
    val pagedResults = gameResultFacade.getSinglePlayerUserResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/single-player/all")
  fun getSinglePlayerAllResults(pageable: Pageable): ResponseEntity<Page<SinglePlayerGameResultDto>> {
    val pagedResults = gameResultFacade.getSinglePlayerAllResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/single-player/{gameId}")
  fun getSinglePlayerResultDetails(@PathVariable("gameId") gameId: Long): ResponseEntity<SinglePlayerGameResultDetailsDto> {
    val resultDetails = gameResultFacade.getSinglePlayerResultDetails(gameId)
    return ResponseEntity(resultDetails, HttpStatus.OK)
  }

  @GetMapping("/multiplayer/user")
  fun getMultiplayerUserResults(pageable: Pageable): ResponseEntity<Page<MultiplayerGameResultDto>> {
    val pagedResults = gameResultFacade.getMultiplayerUserResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/multiplayer/all")
  fun getMultiplayerAllResults(pageable: Pageable): ResponseEntity<Page<MultiplayerGameResultDto>> {
    val pagedResults = gameResultFacade.getMultiplayerAllResults(pageable)
    return ResponseEntity(pagedResults, HttpStatus.OK)
  }

  @GetMapping("/multiplayer/{gameId}")
  fun getMultiplayerResultDetails(@PathVariable("gameId") gameId: Long): ResponseEntity<MultiplayerGameResultDetailsDto> {
    val resultDetails = gameResultFacade.getMultiplayerResultDetails(gameId)
    return ResponseEntity(resultDetails, HttpStatus.OK)
  }
}