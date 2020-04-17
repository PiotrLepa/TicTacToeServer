package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn
import com.piotr.tictactoe.user.dto.UserDto
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MultiplayerGameHelper {

  fun createMultiplayerGame(firstPlayer: UserDto, secondPlayer: UserDto): MultiplayerGame {
    val startingPlayer = getStartingPlayer();
    val (firstPlayerMark, secondPlayerMark) = when (startingPlayer) {
      MultiplayerGameTurn.FIRST_PLAYER -> FieldMark.X to FieldMark.O
      MultiplayerGameTurn.SECOND_PLAYER -> FieldMark.O to FieldMark.X
    }

    return MultiplayerGame(
        firstPlayerId = firstPlayer.id,
        secondPlayerId = secondPlayer.id,
        status = MultiplayerGameStatus.ON_GOING,
        currentTurn = startingPlayer,
        creationDate = DateTime.now().millis,
        firstPlayerMark = firstPlayerMark,
        secondPlayerMark = secondPlayerMark,
        modificationDate = DateTime.now().millis
    )
  }

  fun getStartingPlayer(): MultiplayerGameTurn =
      if (Random.nextBoolean()) MultiplayerGameTurn.FIRST_PLAYER else MultiplayerGameTurn.SECOND_PLAYER
}