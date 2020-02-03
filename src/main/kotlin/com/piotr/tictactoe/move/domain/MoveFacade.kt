package com.piotr.tictactoe.move.domain

import com.piotr.tictactoe.move.domain.model.Move
import com.piotr.tictactoe.move.dto.MoveDto
import com.piotr.tictactoe.move.dto.SetMoveDto
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class MoveFacade {

  @Autowired
  private lateinit var moveRepository: MoveRepository

  fun setMove(dto: SetMoveDto) {
    val lastMove = moveRepository.findMoveByGameIdOrderByCounter(dto.gameId)
    val move = Move(
        gameId = dto.gameId,
        fieldIndex = dto.fieldIndex,
        counter = lastMove?.counter ?: 0,
        mark = dto.mark,
        creationDate = DateTime.now().millis
    )
    moveRepository.save(move)
  }

  fun getAllMoves(gameId: Long): List<MoveDto> = moveRepository.findMovesByGameId(gameId).map(Move::toDto)

  fun createMoveFacade() = MoveFacade()
}