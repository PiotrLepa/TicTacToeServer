package com.piotr.tictactoe.move.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.domain.model.Move
import com.piotr.tictactoe.move.dto.AllMovesDto
import com.piotr.tictactoe.move.dto.MoveDto
import com.piotr.tictactoe.utils.GameConstant.VALID_FIELDS_INDEXES
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MoveFacade {

  @Autowired
  private lateinit var moveRepository: MoveRepository

  @Autowired
  private lateinit var moveDtoConverter: Converter<Move, MoveDto>

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
        .let(moveDtoConverter::convert)
  }

  fun getAllMoves(gameId: Long): AllMovesDto {
    val movies = moveRepository.findMovesByGameId(gameId).map(moveDtoConverter::convert)
    return AllMovesDto(moves = movies)
  }

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
    val allMovies = getAllMoves(gameId)
    if (allMovies.moves.any { it.fieldIndex == fieldIndex }) {
      throw FieldAlreadyTakenException()
    }
  }
}