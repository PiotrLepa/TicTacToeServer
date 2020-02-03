package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GameFacade {

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var gameComponent: GameComponent

  fun createGameWithComputer(difficultyLevel: DifficultyLevel): GameWithComputerDto {
    val player = userFacade.getLoggedUser()
    val game = gameComponent.createGameWithComputer(player, difficultyLevel)
    if (game.currentTurn == GameTurn.COMPUTER) {
      // TODO set first move
    }
    return game.toDto()
  }

  @Bean
  fun create() = GameFacade()
}