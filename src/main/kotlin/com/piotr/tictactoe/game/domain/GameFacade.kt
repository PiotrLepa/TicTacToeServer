package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.util.ComputerMoveComponent
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.MoveFacade
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
        .toDto(listOf())
        .also { gameDto ->
          if (gameComponent.getStartingPlayer() == GameTurn.COMPUTER) {
            setComputerInitialMove(gameDto)
          }
        }
        .let {
          val moves = moveFacade.getAllMoves(it.id)
          it.copy(moves = moves)
        }
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int): GameWithComputerDto {
    val game = gameRepository.findGameById(gameId)
    val move = moveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val allMoves = moveFacade.getAllMoves(gameId)
    return gameRepository.findGameById(gameId)
        .toDto(allMoves)
  }

  private fun setComputerInitialMove(gameDto: GameWithComputerDto) {
    moveFacade.setMove(gameDto.id, computerMoveComponent.getComputerMove(gameDto, listOf()), gameDto.computerMark)
  }

  @Bean
  fun createGameFacade() = GameFacade()
}