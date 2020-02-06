package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import com.piotr.tictactoe.move.domain.model.FieldMark
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class GameComponent {

  fun createGameWithComputer(playerId: Long, difficultyLevel: DifficultyLevel, gameTurn: GameTurn): GameWithComputer {
    val (playerMark, computerMark) = when (gameTurn) {
      GameTurn.PLAYER -> FieldMark.X to FieldMark.O
      GameTurn.COMPUTER -> FieldMark.O to FieldMark.X
    }
    return GameWithComputer(
        playerId = playerId,
        status = GameStatus.ON_GOING,
        difficultyLevel = difficultyLevel,
        playerMark = playerMark,
        computerMark = computerMark,
        creationDate = DateTime.now().millis,
        modificationDate = DateTime.now().millis
    )
  }

  fun getStartingPlayer(): GameTurn = if (Random.nextBoolean()) GameTurn.PLAYER else GameTurn.COMPUTER
}