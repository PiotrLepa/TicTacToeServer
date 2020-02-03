package com.piotr.tictactoe.move.domain

import com.piotr.tictactoe.move.domain.model.Move
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MoveRepository : JpaRepository<Move, Long> {

  fun findMovesByGameId(gameId: Long): List<Move>
}