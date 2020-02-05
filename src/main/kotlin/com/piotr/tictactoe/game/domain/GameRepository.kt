package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.GameWithComputer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<GameWithComputer, Long> {

  fun findGameById(id: Long): GameWithComputer
}