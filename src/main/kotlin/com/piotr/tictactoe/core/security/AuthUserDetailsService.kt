package com.piotr.tictactoe.core.security

import com.piotr.tictactoe.core.security.exception.MissingDeviceToken
import com.piotr.tictactoe.core.security.exception.SecurityUserNotExistsException
import com.piotr.tictactoe.user.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class AuthUserDetailsService : UserDetailsService {

  companion object {
    private const val DEVICE_TOKEN_KEY = "device_token"
  }

  @Autowired
  private lateinit var userRepository: UserRepository

  override fun loadUserByUsername(email: String): UserDetails {
    val user = userRepository.findUserByEmail(email) ?: throw SecurityUserNotExistsException()
    updateFirebaseToken(user)
    return User(user.email, user.password, listOf())
  }

  fun updateFirebaseToken(user: com.piotr.tictactoe.user.domain.model.User) {
    val servletRequest = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
    val deviceId = servletRequest.request.getParameter(DEVICE_TOKEN_KEY) ?: throw MissingDeviceToken()
    userRepository.save(user.copy(deviceToken = deviceId))
  }
}