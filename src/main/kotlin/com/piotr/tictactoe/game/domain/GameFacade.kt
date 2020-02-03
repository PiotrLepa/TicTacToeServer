package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.util.ComputerMoveComponent
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
  private lateinit var computerMoveComponent: ComputerMoveComponent

  @Autowired
  private lateinit var gameRepository: GameRepository

  fun createGameWithComputer(difficultyLevel: DifficultyLevel): GameWithComputerDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = gameComponent.getStartingPlayer()
    return gameComponent.createGameWithComputer(player.id, difficultyLevel, startingPlayer)
        .let(gameRepository::save)
        .toDto()
        .also { gameDto ->
          if (gameComponent.getStartingPlayer() == GameTurn.COMPUTER) {
            setComputerInitialMove(gameDto)
          }
        }
  }

  private fun setComputerInitialMove(gameDto: GameWithComputerDto) {
    moveFacade.setMove(SetMoveDto(
        gameId = gameDto.id,
        fieldIndex = computerMoveComponent.getComputerMove(gameDto, listOf()),
        mark = gameDto.computerMark
    ))
  }

  @Bean
  fun createGameFacade() = GameFacade()
}