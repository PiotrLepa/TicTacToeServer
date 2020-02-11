package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

  fun findByUsername(username: String): User?

  fun findUserByEmail(email: String): User?
}