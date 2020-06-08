package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn
import com.piotr.tictactoe.multiplayerGame.exception.GameAlreadyStaredException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.multiplayerGame.exception.InvalidPlayerException
import com.piotr.tictactoe.multiplayerGame.exception.OpponentMoveException
import com.piotr.tictactoe.singlePlayerGame.exception.GameFinishedException
import com.piotr.tictactoe.singlePlayerGame.exception.GameNotFinishedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.stereotype.Component

@Component
class MultiplayerGameChecker {

  fun checkIfPlayerInvitedHimself(player: UserDto, opponent: UserDto) {
    if (player.id == opponent.id) {
      throw InvalidOpponentCodeException()
    }
  }

  fun checkIfOpponentIsCorrect(game: MultiplayerGame, player: UserDto) {
    if (game.secondPlayerId != player.id) {
      throw InvalidPlayerException()
    }
  }

  fun checkIfGameHasNotStarted(game: MultiplayerGame) {
    if (game.status != MultiplayerGameStatus.CREATED) {
      throw GameAlreadyStaredException()
    }
  }

  fun checkGameTurn(game: MultiplayerGame, player: UserDto) {
    val correctPlayer = when (game.currentTurn) {
      MultiplayerGameTurn.FIRST_PLAYER -> game.firstPlayerId == player.id
      MultiplayerGameTurn.SECOND_PLAYER -> game.secondPlayerId == player.id
    }
    if (!correctPlayer) {
      throw OpponentMoveException()
    }
  }

  fun checkIfPlayerMatch(game: MultiplayerGame, player: UserDto) {
    if (player.id != game.firstPlayerId && player.id != game.secondPlayerId) {
      throw WrongPlayerException()
    }
  }

  fun checkIfGameIsOnGoing(game: MultiplayerGame) {
    if (game.status != MultiplayerGameStatus.ON_GOING) {
      throw GameFinishedException()
    }
  }

  fun checkIfGameFinished(game: MultiplayerGame) {
    if (game.status !in MultiplayerGameStatus.getFinishedGameStatus()) {
      throw GameNotFinishedException()
    }
  }
}