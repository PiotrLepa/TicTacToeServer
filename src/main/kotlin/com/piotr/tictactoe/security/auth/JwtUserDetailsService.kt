package com.piotr.tictactoe.security.auth

import com.piotr.tictactoe.user.UserDao
import com.piotr.tictactoe.user.UserDto
import com.piotr.tictactoe.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService : UserDetailsService {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var bcryptEncoder: PasswordEncoder

  override fun loadUserByUsername(username: String): UserDetails {
    val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")
    return User(user.username, user.password, listOf())
  }

  fun save(user: UserDto): UserDao {
    val newUser = UserDao(null, user.username, bcryptEncoder.encode(user.password))
    return userRepository.save(newUser)
  }

}