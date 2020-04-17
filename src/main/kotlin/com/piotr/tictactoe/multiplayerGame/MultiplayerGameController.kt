package com.piotr.tictactoe.multiplayerGame

import com.piotr.tictactoe.multiplayerGame.domain.MultiplayerGameFacade
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MultiplayerGameController {

  @Autowired
  private lateinit var multiplayerGameFacade: MultiplayerGameFacade

  @PostMapping("/create")
  fun createMultiplayerGame(@RequestParam("opponent_code") opponentCode: String): ResponseEntity<MultiplayerGameCreatedDto> {
    val game = multiplayerGameFacade.createMultiplayerGame(opponentCode)
    return ResponseEntity(game, HttpStatus.CREATED)
  }

  @PutMapping("/{gameId}/move/{fieldIndex}")
  fun setMove(
    @PathVariable("gameId") gameId: Long,
    @PathVariable("fieldIndex") fieldIndex: Int
  ): ResponseEntity<Unit> {
    multiplayerGameFacade.setPlayerMove(gameId, fieldIndex)
    return ResponseEntity(HttpStatus.OK)
  }
}