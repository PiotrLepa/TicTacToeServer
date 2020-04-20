package com.piotr.tictactoe.singlePlayerGame.domain.utils

import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import com.piotr.tictactoe.singlePlayerGame.exception.GameEndedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.stereotype.Component

@Component
class SinglePlayerGameChecker {

  fun checkIfPlayerMatch(game: SinglePlayerGame, player: UserDto) {
    if (player.id != game.playerId) {
      throw WrongPlayerException()
    }
  }

  fun checkIfGameIsOnGoing(game: SinglePlayerGame) {
    if (game.status != SinglePlayerGameStatus.ON_GOING) {
      throw GameEndedException()
    }
  }
}