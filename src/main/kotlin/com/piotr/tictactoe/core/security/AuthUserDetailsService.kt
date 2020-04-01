package com.piotr.tictactoe.core.security

import com.piotr.tictactoe.core.security.exception.SecurityUserNotExistsException
import com.piotr.tictactoe.user.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService : UserDetailsService {

  @Autowired
  private lateinit var userRepository: UserRepository

  override fun loadUserByUsername(email: String): UserDetails {
    val user = userRepository.findUserByEmail(email) ?: throw SecurityUserNotExistsException()
    return User(user.email, user.password, listOf())
  }
}