package com.piotr.tictactoe.user.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.user.converter.RegisterEntityConverterArgs
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.domain.utils.UserChecker
import com.piotr.tictactoe.user.dto.RegisterDto
import com.piotr.tictactoe.user.dto.UserDto
import com.piotr.tictactoe.user.exception.EmailAlreadyExistsException
import com.piotr.tictactoe.user.exception.UsernameAlreadyExistsException
import com.piotr.tictactoe.utils.PlayerCodeGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import sun.plugin.dom.exception.InvalidStateException
import java.util.Locale

@Service
class UserFacade @Autowired constructor(
  private val userRepository: UserRepository,
  private val playerCodeGenerator: PlayerCodeGenerator,
  private val userChecker: UserChecker,
  private val userDtoConverter: Converter<User, UserDto>,
  private val registerEntityConverter: ConverterWithArgs<RegisterDto, User, RegisterEntityConverterArgs>
) {

  fun register(dto: RegisterDto): UserDto {
    userChecker.checkUsernameLength(dto)
    userChecker.checkPasswordLength(dto)
    userChecker.checkIfPasswordsAreTheSame(dto)
    checkIfEmailIsUnique(dto)
    checkIfUsernameIsUnique(dto)
    val converterArgs = RegisterEntityConverterArgs(
        languageTag = LocaleContextHolder.getLocale().toLanguageTag(),
        deviceToken = "",
        playerCode = getUniquePlayerCode()
    )
    val entity = registerEntityConverter.convert(dto, converterArgs)
    return userRepository.save(entity).let(userDtoConverter::convert)
  }

  fun getLoggedInUser(): UserDto {
    val email = getAuthenticatedUserEmail() ?: throw InvalidStateException("No logged in user found")
    return findUserByEmail(email) ?: throw InvalidStateException("No logged in user found")
  }

  fun findUserByPlayerCode(code: String): UserDto? =
      userRepository.findUserByPlayerCode(code)?.let(userDtoConverter::convert)

  fun findUserById(id: Long): UserDto =
      userRepository.findById(id).get().let(userDtoConverter::convert)

  fun updateUserLocale(locale: Locale) {
    val email = getAuthenticatedUserEmail() ?: return
    val user = userRepository.findUserByEmail(email) ?: return
    userRepository.save(user.copy(languageTag = locale.toLanguageTag()))
  }

  private fun findUserByEmail(email: String): UserDto? =
      userRepository.findUserByEmail(email)?.let(userDtoConverter::convert)

  private fun getAuthenticatedUserEmail(): String? = SecurityContextHolder.getContext().authentication?.name

  private fun getUniquePlayerCode(): String {
    val playerCode = playerCodeGenerator.generate()
    return if (userRepository.findUserByPlayerCode(playerCode) == null) {
      playerCode
    } else {
      getUniquePlayerCode()
    }
  }

  fun checkIfEmailIsUnique(dto: RegisterDto) {
    if (userRepository.findUserByEmail(dto.email) != null) {
      throw EmailAlreadyExistsException()
    }
  }

  fun checkIfUsernameIsUnique(dto: RegisterDto) {
    if (userRepository.findUserByUsername(dto.username) != null) {
      throw UsernameAlreadyExistsException()
    }
  }
}