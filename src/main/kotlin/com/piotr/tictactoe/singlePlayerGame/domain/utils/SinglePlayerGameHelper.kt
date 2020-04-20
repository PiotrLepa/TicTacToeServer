package com.piotr.tictactoe.singlePlayerGame.domain.utils

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameTurn
import com.piotr.tictactoe.utils.GameEndChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class SinglePlayerGameHelper @Autowired constructor(
  private val gameEndChecker: GameEndChecker
) {

  fun createSinglePlayerGame(playerId: Long, difficultyLevel: DifficultyLevel, gameTurn: SinglePlayerGameTurn): SinglePlayerGame {
    val (playerMark, computerMark) = when (gameTurn) {
      SinglePlayerGameTurn.PLAYER -> FieldMark.X to FieldMark.O
      SinglePlayerGameTurn.COMPUTER -> FieldMark.O to FieldMark.X
    }
    return SinglePlayerGame(
        playerId = playerId,
        status = SinglePlayerGameStatus.ON_GOING,
        difficultyLevel = difficultyLevel,
        playerMark = playerMark,
        computerMark = computerMark
    )
  }

  fun getStartingPlayer(): SinglePlayerGameTurn = if (Random.nextBoolean()) SinglePlayerGameTurn.PLAYER else SinglePlayerGameTurn.COMPUTER

  fun getGameStatus(
    moves: List<GameMoveDto>,
    playerMark: FieldMark
  ): SinglePlayerGameStatus = when (gameEndChecker.checkGameEnd(moves, playerMark)) {
    GameEndChecker.GameResultType.WON -> SinglePlayerGameStatus.PLAYER_WON
    GameEndChecker.GameResultType.LOST -> SinglePlayerGameStatus.COMPUTER_WON
    GameEndChecker.GameResultType.DRAW -> SinglePlayerGameStatus.DRAW
    GameEndChecker.GameResultType.ON_GOING -> SinglePlayerGameStatus.ON_GOING
  }

  fun isGameOnGoing(moves: List<GameMoveDto>, playerMark: FieldMark) =
      getGameStatus(moves, playerMark) == SinglePlayerGameStatus.ON_GOING
}