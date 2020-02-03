package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.MoveFacade
import com.piotr.tictactoe.move.dto.SetMoveDto
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GameFacade {

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var moveFacade: MoveFacade

  @Autowired
  private lateinit var gameComponent: GameComponent

  @Autowired
  private lateinit var gameRepository: GameRepository

  fun createGameWithComputer(difficultyLevel: DifficultyLevel): GameWithComputerDto {
    val player = userFacade.getLoggedUser()
    val game = gameComponent.createGameWithComputer(player, difficultyLevel)
        .let(gameRepository::save)
    if (game.currentTurn == GameTurn.COMPUTER) {
      moveFacade.setMove(SetMoveDto(
          gameId = game.id!!,
          fieldIndex = 4, // TODO
          mark = game.computerMark
      ))
    }
    return game.toDto()
  }

  @Bean
  fun createGameFacade() = GameFacade()
}