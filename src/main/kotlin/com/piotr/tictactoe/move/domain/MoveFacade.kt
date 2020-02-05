package com.piotr.tictactoe.move.domain

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
    checkFieldIndex(fieldIndex)
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

  private fun checkFieldIndex(fieldIndex: Int) {
    if (fieldIndex !in validFieldsIndexes) {
      throw InvalidFieldIndexRangeException("Field index should be in range $validFieldsIndexes")
    }
  }

  private fun getNextCounter(gameId: Long): Int {
    val lastMove = moveRepository.findFirstMoveByGameIdOrderByIdDesc(gameId)
    return lastMove?.counter?.plus(1) ?: 0
  }

  fun createMoveFacade() = MoveFacade()

  companion object {
    val FIELD_MIN_INDEX = 0
    val FIELD_MAX_INDEX = 8
    private val validFieldsIndexes = (FIELD_MIN_INDEX..FIELD_MAX_INDEX)
  }
}