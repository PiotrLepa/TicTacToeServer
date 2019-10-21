package com.piotr.tictactoe.domain.component.impl

import com.piotr.tictactoe.domain.component.AiMoveComponent
import com.piotr.tictactoe.domain.component.GameComponent
import com.piotr.tictactoe.domain.dto.DifficultyLevel
import com.piotr.tictactoe.domain.dto.FieldDto
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.GameStatus
import com.piotr.tictactoe.domain.dto.Mark
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Random

@Component
class GameComponentImpl : GameComponent {

  @Autowired
  private lateinit var aiMoveComponent: AiMoveComponent

  override fun createGameBoard(difficultyLevel: DifficultyLevel): GameDto {
    val turn = getPlayerWhichStart()

    val playerMark = if (turn == GameStatus.PLAYER_TURN) Mark.X else Mark.O
    val aiMark = if (turn == GameStatus.AI_TURN) Mark.X else Mark.O
    val gameDto = GameDto(System.nanoTime(), GameStatus.PLAYER_TURN, difficultyLevel, createEmptyBoard(), playerMark, aiMark)

    return if (turn == GameStatus.PLAYER_TURN) {
      gameDto
    } else {
      aiMoveComponent.setFieldByAi(gameDto)
    }
  }

  override fun setField(game: GameDto): GameDto {
    // TODO check if gameDto from user is correct compare to saved gameDto
    return aiMoveComponent.setFieldByAi(game)
  }

  private fun getPlayerWhichStart(): GameStatus =
      if (Random().nextBoolean()) GameStatus.PLAYER_TURN else GameStatus.AI_TURN

  private fun createEmptyBoard(): List<FieldDto> =
      MutableList(9) { index -> FieldDto(index, Mark.EMPTY) }
}