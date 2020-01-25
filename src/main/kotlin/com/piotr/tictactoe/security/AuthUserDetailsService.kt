package com.piotr.tictactoe.security

import com.piotr.tictactoe.user.UserDao
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

  override fun loadUserByUsername(username: String): UserDetails? {
    val user = userRepository.findByUsername(username) ?: return null
    return User(user.username, user.password, listOf())
  }

  fun save(user: UserDto): UserDao {
    val newUser = UserDao(null, user.username, passwordEncoder.encode(user.password))
    return userRepository.save(newUser)
  }
}