package com.piotr.tictactoe.multiplayerGame.domain.utils

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameTurn
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.utils.GameEndChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MultiplayerGameHelper @Autowired constructor(
  private val gameEndChecker: GameEndChecker
) {

  fun createMultiplayerGame(firstPlayer: UserDto, secondPlayer: UserDto): MultiplayerGame {
    val startingPlayer = getStartingPlayer();
    val (firstPlayerMark, secondPlayerMark) = when (startingPlayer) {
      MultiplayerGameTurn.FIRST_PLAYER -> FieldMark.X to FieldMark.O
      MultiplayerGameTurn.SECOND_PLAYER -> FieldMark.O to FieldMark.X
    }

    return MultiplayerGame(
        firstPlayerId = firstPlayer.id,
        secondPlayerId = secondPlayer.id,
        socketDestination = createSocketDestination(firstPlayer.playerCode, secondPlayer.playerCode),
        status = MultiplayerGameStatus.CREATED,
        currentTurn = startingPlayer,
        firstPlayerMark = firstPlayerMark,
        secondPlayerMark = secondPlayerMark
    )
  }

  fun getGameResult(moves: List<GameMoveDto>, firstPlayer: FieldMark): MultiplayerGameStatus =
      when (gameEndChecker.checkGameEnd(moves, firstPlayer)) {
        GameEndChecker.GameResultType.WON -> MultiplayerGameStatus.FIRST_PLAYER_WON
        GameEndChecker.GameResultType.LOST -> MultiplayerGameStatus.SECOND_PLAYER_WON
        GameEndChecker.GameResultType.DRAW -> MultiplayerGameStatus.DRAW
        GameEndChecker.GameResultType.ON_GOING -> MultiplayerGameStatus.ON_GOING
      }

  fun getMarkForPlayer(game: MultiplayerGame, player: UserDto): FieldMark =
      if (game.firstPlayerId == player.id) {
        game.firstPlayerMark
      } else {
        game.secondPlayerMark
      }

  private fun getStartingPlayer(): MultiplayerGameTurn =
      if (Random.nextBoolean()) MultiplayerGameTurn.FIRST_PLAYER else MultiplayerGameTurn.SECOND_PLAYER

  private fun createSocketDestination(firstPlayerCode: String, secondPlayerCode: String) =
      firstPlayerCode + secondPlayerCode
}