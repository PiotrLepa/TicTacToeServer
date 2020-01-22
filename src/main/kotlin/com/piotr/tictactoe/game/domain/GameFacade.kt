package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.PlayerMove
import com.piotr.tictactoe.game.domain.model.ResetBoard
import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.dto.GameDto
import com.piotr.tictactoe.game.dto.PlayerMoveDto
import com.piotr.tictactoe.game.dto.ResetBoardDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean

class GameFacade {

  @Autowired
  private lateinit var gameComponent: GameComponent

  @Autowired
  private lateinit var gameRepository: GameRepository

  fun createGame(): GameDto {
    val game = gameComponent.createGame(DifficultyLevel.MEDIUM, System.currentTimeMillis())
    return gameRepository.save(game).toDto()
  }

  fun setField(playerMoveDto: PlayerMoveDto): GameDto {
    // TODO check if move is correct
    val playerMove = PlayerMove.fromDto(playerMoveDto)
    val game = gameRepository.getOne(playerMove.gameId)
    return gameComponent.setField(game, playerMove)
        .let(gameRepository::save)
        .toDto()
  }

  fun resetBoard(resetBoardDto: ResetBoardDto): GameDto {
    val resetBoard = ResetBoard.fromDto(resetBoardDto)
    val game = gameRepository.getOne(resetBoard.gameId)
    return gameComponent.resetBoard(game)
        .let(gameRepository::save)
        .toDto()
  }

  @Bean
  fun create() = GameFacade()
}