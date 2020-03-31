package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.GameStatus
import com.piotr.tictactoe.game.domain.model.GameWithComputer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<GameWithComputer, Long> {

  fun findGameByGameId(id: Long): GameWithComputer

  fun findAllByStatusIn(pageable: Pageable, status: List<GameStatus>): Page<GameWithComputer>

  fun findAllByStatusInAndPlayerId(pageable: Pageable, status: List<GameStatus>, playerId: Long): Page<GameWithComputer>
}