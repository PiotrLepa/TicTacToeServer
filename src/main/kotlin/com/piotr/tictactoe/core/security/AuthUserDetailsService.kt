package com.piotr.tictactoe.core.security

import com.piotr.tictactoe.core.security.config.OAuth2ServerConfig.Companion.GRANT_TYPE_PASSWORD
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
class AuthUserDetailsService @Autowired constructor(
  private val userRepository: UserRepository
) : UserDetailsService {

  override fun loadUserByUsername(email: String): UserDetails {
    val user = userRepository.findUserByEmail(email) ?: throw SecurityUserNotExistsException()
    updateFirebaseToken(user)
    return User(user.email, user.password, listOf())
  }

  fun updateFirebaseToken(user: com.piotr.tictactoe.user.domain.model.User) {
    val servletRequest = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
    val request = servletRequest.request
    if (request.getParameter(GRANT_TYPE_KEY) != GRANT_TYPE_PASSWORD) {
      return
    }
    val deviceId = request.getParameter(DEVICE_TOKEN_KEY) ?: throw MissingDeviceToken()
    userRepository.save(user.copy(deviceToken = deviceId))
  }

  companion object {
    private const val GRANT_TYPE_KEY = "grant_type"
    private const val DEVICE_TOKEN_KEY = "device_token"
  }
}