package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.domain.model.DifficultyLevel
import com.piotr.tictactoe.game.domain.model.Field
import com.piotr.tictactoe.game.domain.model.Game
import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.Mark
import com.piotr.tictactoe.game.domain.model.PlayerMove
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Random

@Component
class GameComponent {

  @Autowired
  private lateinit var aiMoveComponent: AiMoveComponent

  fun createGame(difficultyLevel: DifficultyLevel, gameId: Long): Game {
    val playerMark = getPlayerMark()
    val aiMark = if (playerMark == Mark.X) Mark.O else Mark.X
    val gameDto = Game(
        gameId = gameId,
        difficultyLevel = difficultyLevel,
        board = createEmptyBoard(),
        playerMark = playerMark,
        aiMark = aiMark,
        status = GameStatus.IN_GAME,
        playerWins = 0,
        draws = 0,
        playerDefeats = 0
    )
    return setupInitiallyBoard(gameDto)
  }

  fun resetBoard(game: Game): Game {
    val newGame = game.copy(board = createEmptyBoard(), status = GameStatus.IN_GAME)
    return setupInitiallyBoard(newGame)
  }

  private fun setupInitiallyBoard(game: Game): Game =
      if (game.playerMark == STARTING_PLAYER) {
        game
      } else {
        aiMoveComponent.setFieldByAi(game)
      }

  fun setField(game: Game, playerMove: PlayerMove): Game {
    val field = playerMove.field
    game.board[field.index].mark = field.mark
    return aiMoveComponent.setFieldByAi(game)
  }

  private fun getPlayerMark(): Mark = if (Random().nextBoolean()) Mark.X else Mark.O

  private fun createEmptyBoard(): List<Field> =
      MutableList(9) { index ->
        Field(null, index, Mark.EMPTY)
      }

  companion object {
    private val STARTING_PLAYER = Mark.X
  }
}