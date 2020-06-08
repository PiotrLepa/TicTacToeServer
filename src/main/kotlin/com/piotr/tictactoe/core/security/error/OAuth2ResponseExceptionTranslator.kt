package com.piotr.tictactoe.core.security.error

import com.piotr.tictactoe.core.extensions.getLocalizedMessage
import com.piotr.tictactoe.core.security.exception.SecurityUserNotExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator

@Configuration
class OAuth2ResponseExceptionTranslator @Autowired constructor(
  private val messageSource: MessageSource
) : DefaultWebResponseExceptionTranslator() {

  override fun translate(exception: Exception): ResponseEntity<OAuth2Exception> {
    val responseEntity = super.translate(exception)
    val customException = responseEntity.body
        ?.apply { addCustomProperties(this) }
        ?: return responseEntity
    return ResponseEntity.status(customException.httpErrorCode)
        .body(customException)
  }

  private fun addCustomProperties(oAuth2Exception: OAuth2Exception) {
    with(oAuth2Exception) {
      addAdditionalInformation("code", httpErrorCode.toString())
      addAdditionalInformation("developerMessage", this::class.simpleName.toString())
      val printableMessage = getMessageCodeForException(this)?.let { messageSource.getLocalizedMessage(it) }
      addAdditionalInformation("printableMessage", printableMessage)
    }
  }

  private fun getMessageCodeForException(exception: OAuth2Exception): String? =
      if (exception is SecurityUserNotExistsException || exception.message == "Bad credentials") {
        "security.error.user_not_exists"
      } else {
        null
      }
}