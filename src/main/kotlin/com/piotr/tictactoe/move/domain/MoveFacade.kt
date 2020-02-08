package com.piotr.tictactoe.move.domain

import com.piotr.tictactoe.game.domain.util.GameConstant.VALID_FIELDS_INDEXES
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.domain.model.Move
import com.piotr.tictactoe.move.dto.MoveDto
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class MoveFacade {

  @Autowired
  private lateinit var moveRepository: MoveRepository

  fun setMove(gameId: Long, fieldIndex: Int, mark: FieldMark): MoveDto {
    checkFieldIndexRange(fieldIndex)
    checkIfFieldIndexIsEmpty(gameId, fieldIndex)
    return Move(
        gameId = gameId,
        fieldIndex = fieldIndex,
        counter = getNextCounter(gameId),
        mark = mark,
        creationDate = DateTime.now().millis
    ).let(moveRepository::save)
        .toDto()
  }

  fun getAllMoves(gameId: Long): List<MoveDto> = moveRepository.findMovesByGameId(gameId).map(Move::toDto)

  private fun getNextCounter(gameId: Long): Int {
    val lastMove = moveRepository.findFirstMoveByGameIdOrderByMoveIdDesc(gameId)
    return lastMove?.counter?.plus(1) ?: 0
  }

  private fun checkFieldIndexRange(fieldIndex: Int) {
    if (fieldIndex !in VALID_FIELDS_INDEXES) {
      throw InvalidFieldIndexRangeException("Field index should be in range $VALID_FIELDS_INDEXES")
    }
  }

  private fun checkIfFieldIndexIsEmpty(gameId: Long, fieldIndex: Int) {
    val moves = getAllMoves(gameId)
    if (moves.any { it.fieldIndex == fieldIndex }) {
      throw FieldAlreadyTakenException()
    }
  }

  fun createMoveFacade() = MoveFacade()
}