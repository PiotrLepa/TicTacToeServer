package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.user.dto.UserDto
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class GameComponent {

  fun createGameWithComputer(player: UserDto, difficultyLevel: DifficultyLevel): GameWithComputer {
    val startingPlayer = getStartingPlayer()
    val playerMark = if (startingPlayer == GameTurn.PLAYER) FieldMark.X else FieldMark.O
    val computerMark = if (startingPlayer == GameTurn.PLAYER) FieldMark.O else FieldMark.X
    return GameWithComputer(
        playerId = player.id,
        status = GameStatus.IN_PROGRESS,
        difficultyLevel = difficultyLevel,
        currentTurn = startingPlayer,
        playerMark = playerMark,
        computerMark = computerMark,
        creationDate = DateTime.now().millis,
        modificationDate = DateTime.now().millis
    )
  }

  private fun getStartingPlayer(): GameTurn = if (Random.nextBoolean()) GameTurn.PLAYER else GameTurn.COMPUTER
}