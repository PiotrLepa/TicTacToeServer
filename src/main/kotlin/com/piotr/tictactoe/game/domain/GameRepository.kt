package com.piotr.tictactoe.game.domain

import com.piotr.tictactoe.game.domain.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<Game, Long>