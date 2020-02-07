package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus.ON_GOING
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.domain.util.GameConstant.FIELD_MAX_INDEX
import com.piotr.tictactoe.game.domain.util.GameEndChecker
import com.piotr.tictactoe.game.domain.util.computermove.ComputerMoveGetter
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.MoveFacade
import com.piotr.tictactoe.move.dto.MoveDto
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
  private lateinit var computerMoveGetter: ComputerMoveGetter

  @Autowired
  private lateinit var gameEndChecker: GameEndChecker

  @Autowired
  private lateinit var gameRepository: GameRepository

  fun createGameWithComputer(difficultyLevel: DifficultyLevel): GameWithComputerDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = gameComponent.getStartingPlayer()
    return gameComponent.createGameWithComputer(player.id, difficultyLevel, startingPlayer)
        .let(gameRepository::save)
        .toDto(listOf())
        .let { gameDto ->
          if (gameComponent.getStartingPlayer() == GameTurn.COMPUTER) {
            setComputeMove(gameDto)
          } else {
            gameDto
          }
        }
  }

  fun setPlayerMoveAndGetComputerMove(gameId: Long, fieldIndex: Int): GameWithComputerDto {
    val game = gameRepository.findGameById(gameId)
    checkIfGameIsOnGoing(game)
    val move = moveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val gameDto = game.toDto(moveFacade.getAllMoves(gameId))
    return if (canDoNextMove(move)) {
      updateGameStatatus(setComputeMove(gameDto))
    } else {
      updateGameStatatus(gameDto)
    }
  }

  private fun setComputeMove(gameDto: GameWithComputerDto): GameWithComputerDto {
    val computerMoveFieldIndex = computerMoveGetter.getComputerMove(gameDto)
    val move = moveFacade.setMove(gameDto.gameId, computerMoveFieldIndex, gameDto.computerMark)
    return gameDto.copy(moves = gameDto.moves + listOf(move))
  }

  private fun updateGameStatatus(gameDto: GameWithComputerDto): GameWithComputerDto =
      gameDto.copy(status = gameEndChecker.checkGameEnd(gameDto))

  private fun canDoNextMove(move: MoveDto) = move.fieldIndex != FIELD_MAX_INDEX

  private fun checkIfGameIsOnGoing(game: GameWithComputer) {
    if (game.status != ON_GOING) {
      throw GameEndedException()
    }
  }

  @Bean
  fun createGameFacade() = GameFacade()
}