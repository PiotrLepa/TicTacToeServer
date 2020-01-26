package com.piotr.tictactoe.security

import com.piotr.tictactoe.user.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService : UserDetailsService {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  override fun loadUserByUsername(email: String): UserDetails? {
    return User("piotr43a@gmail.com", passwordEncoder.encode("pass"), listOf()) // TODO get user from repository
    val user = userRepository.findUserByEmail(email) ?: return null
    return User(user.email, user.password, listOf())
  }
}