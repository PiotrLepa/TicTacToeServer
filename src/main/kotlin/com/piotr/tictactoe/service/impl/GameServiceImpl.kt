package com.piotr.tictactoe.service.impl

import com.piotr.tictactoe.domain.component.GameComponent
import com.piotr.tictactoe.domain.dto.DifficultyLevel
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.resource.FileResource
import com.piotr.tictactoe.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameServiceImpl : GameService {

  @Autowired
  private lateinit var gameComponent: GameComponent

  @Autowired
  private lateinit var fileResource: FileResource

  override fun createGame(): GameDto {
    val game = gameComponent.createGameBoard(DifficultyLevel.MEDIUM)
    fileResource.saveGame(game)
    return game
  }

  override fun setField(game: GameDto): GameDto {
    val updatedGame = gameComponent.setField(game)
    fileResource.saveGame(updatedGame)
    return updatedGame
  }
}