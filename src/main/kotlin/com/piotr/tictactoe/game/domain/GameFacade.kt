package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.util.GameComponent
import com.piotr.tictactoe.game.domain.util.GameSaver
import com.piotr.tictactoe.game.dto.DifficultyLevel
import com.piotr.tictactoe.game.dto.GameDto
import com.piotr.tictactoe.game.dto.PlayerMoveDto
import com.piotr.tictactoe.game.dto.ResetBoardDto
import org.springframework.beans.factory.annotation.Autowired

class GameFacade {

  @Autowired
  private lateinit var gameComponent: GameComponent

  @Autowired
  private lateinit var gameSaver: GameSaver

  fun createGame(): GameDto {
    val game = gameComponent.createGame(DifficultyLevel.MEDIUM, System.currentTimeMillis())
    gameSaver.saveGame(game)
    return game
  }

  fun setField(playerMove: PlayerMoveDto): GameDto {
    val game = gameSaver.loadGame(playerMove.gameId)
    // TODO check if move is correct
    return gameComponent.setField(game, playerMove)
        .also(gameSaver::saveGame)
  }

  fun resetBoard(resetBoard: ResetBoardDto): GameDto {
    val game = gameSaver.loadGame(resetBoard.gameId)
    return gameComponent.resetBoard(game)
        .also(gameSaver::saveGame)
  }
}