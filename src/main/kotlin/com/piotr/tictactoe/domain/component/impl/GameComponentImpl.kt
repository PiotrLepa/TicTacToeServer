package com.piotr.tictactoe.domain.component.impl

import com.piotr.tictactoe.domain.component.AiMoveComponent
import com.piotr.tictactoe.domain.component.GameComponent
import com.piotr.tictactoe.domain.dto.DifficultyLevel
import com.piotr.tictactoe.domain.dto.FieldDto
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.GameStatus
import com.piotr.tictactoe.domain.dto.Mark
import com.piotr.tictactoe.domain.dto.PlayerMoveDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Random

@Component
class GameComponentImpl : GameComponent {

  @Autowired
  private lateinit var aiMoveComponent: AiMoveComponent

  override fun createGame(
    difficultyLevel: DifficultyLevel,
    gameId: Long
  ): GameDto {

    val playerMark = getPlayerMark()
    val aiMark = if (playerMark == Mark.X) Mark.O else Mark.X
    val gameDto =
        GameDto(gameId, difficultyLevel, createEmptyBoard(), playerMark, aiMark, GameStatus.IN_GAME,
            0, 0, 0)

    return setupInitiallyBoard(gameDto)
  }

  override fun resetBoard(
    game: GameDto
  ): GameDto {
    val game = game.copy(board = createEmptyBoard())

    return setupInitiallyBoard(game)
  }

  private fun setupInitiallyBoard(
    gameDto: GameDto
  ): GameDto = if (gameDto.playerMark == STARTING_PLAYER) {
    gameDto
  } else {
    aiMoveComponent.setFieldByAi(gameDto)
  }

  override fun setField(
    game: GameDto,
    playerMove: PlayerMoveDto
  ): GameDto {
    val (index, mark) = playerMove.field
    game.board[index].mark = mark
    return aiMoveComponent.setFieldByAi(game)
  }

  private fun getPlayerMark(): Mark =
      if (Random().nextBoolean()) Mark.X else Mark.O

  private fun createEmptyBoard(): List<FieldDto> =
      MutableList(9) { index -> FieldDto(index, Mark.EMPTY) }

  companion object {
    private val STARTING_PLAYER = Mark.X
  }
}