package com.piotr.tictactoe.game.domain.util

import com.piotr.tictactoe.game.dto.DifficultyLevel
import com.piotr.tictactoe.game.dto.FieldDto
import com.piotr.tictactoe.game.dto.GameDto
import com.piotr.tictactoe.game.dto.GameStatus
import com.piotr.tictactoe.game.dto.Mark
import com.piotr.tictactoe.game.dto.PlayerMoveDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Random

@Component
class GameComponent {

  @Autowired
  private lateinit var aiMoveComponent: AiMoveComponent

  fun createGame(difficultyLevel: DifficultyLevel, gameId: Long): GameDto {
    val playerMark = getPlayerMark()
    val aiMark = if (playerMark == Mark.X) Mark.O else Mark.X
    val gameDto = GameDto(
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

  fun resetBoard(game: GameDto): GameDto {
    val newGame = game.copy(board = createEmptyBoard(), status = GameStatus.IN_GAME)
    return setupInitiallyBoard(newGame)
  }

  private fun setupInitiallyBoard(gameDto: GameDto): GameDto =
      if (gameDto.playerMark == STARTING_PLAYER) {
        gameDto
      } else {
        aiMoveComponent.setFieldByAi(gameDto)
      }

  fun setField(game: GameDto, playerMove: PlayerMoveDto): GameDto {
    val (index, mark) = playerMove.field
    game.board[index].mark = mark
    return aiMoveComponent.setFieldByAi(game)
  }

  private fun getPlayerMark(): Mark = if (Random().nextBoolean()) Mark.X else Mark.O

  private fun createEmptyBoard(): List<FieldDto> =
      MutableList(9) { index ->
        FieldDto(index, Mark.EMPTY)
      }

  companion object {
    private val STARTING_PLAYER = Mark.X
  }
}