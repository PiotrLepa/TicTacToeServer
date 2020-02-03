package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameTurn
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import com.piotr.tictactoe.user.dto.UserDto
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class GameComponent {

  fun createGameWithComputer(player: UserDto, difficultyLevel: DifficultyLevel): GameWithComputer =
      GameWithComputer(
          playerId = player.id,
          status = GameStatus.IN_PROGRESS,
          difficultyLevel = difficultyLevel,
          currentTurn = getStartingPlayer(),
          creationDate = DateTime.now().millis,
          modificationDate = DateTime.now().millis
      )

  private fun getStartingPlayer(): GameTurn = if (Random.nextBoolean()) GameTurn.PLAYER else GameTurn.COMPUTER
}