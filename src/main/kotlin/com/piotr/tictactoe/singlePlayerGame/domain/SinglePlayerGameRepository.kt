package com.piotr.tictactoe.singlePlayerGame.domain

import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SinglePlayerGameRepository : JpaRepository<SinglePlayerGame, Long> {

  fun findGameByGameId(id: Long): SinglePlayerGame

  fun findAllByStatusInOrderByModificationDateDesc(pageable: Pageable, status: List<SinglePlayerGameStatus>): Page<SinglePlayerGame>

  fun findAllByStatusInAndPlayerIdOrderByModificationDateDesc(
    pageable: Pageable,
    status: List<SinglePlayerGameStatus>,
    playerId: Long
  ): Page<SinglePlayerGame>
}