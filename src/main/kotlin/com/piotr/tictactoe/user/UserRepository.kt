package com.piotr.tictactoe.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserDao, Long> {

  fun findByUsername(username: String): UserDao?
}