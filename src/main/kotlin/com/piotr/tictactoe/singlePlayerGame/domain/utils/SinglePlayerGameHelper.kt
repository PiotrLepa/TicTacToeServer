package com.piotr.tictactoe.singlePlayerGame.domain.utils

import com.piotr.tictactoe.common.game.model.FieldMark
import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameTurn
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class SinglePlayerGameHelper {

  fun createSinglePlayerGame(playerId: Long, difficultyLevel: DifficultyLevel, gameTurn: SinglePlayerGameTurn): SinglePlayerGame {
    val (playerMark, computerMark) = when (gameTurn) {
      SinglePlayerGameTurn.PLAYER -> FieldMark.X to FieldMark.O
      SinglePlayerGameTurn.COMPUTER -> FieldMark.O to FieldMark.X
    }
    val creationDate = DateTime.now().millis
    return SinglePlayerGame(
        playerId = playerId,
        status = GameStatus.ON_GOING,
        difficultyLevel = difficultyLevel,
        playerMark = playerMark,
        computerMark = computerMark,
        creationDate = creationDate,
        modificationDate = creationDate
    )
  }

  fun getStartingPlayer(): SinglePlayerGameTurn = if (Random.nextBoolean()) SinglePlayerGameTurn.PLAYER else SinglePlayerGameTurn.COMPUTER
}