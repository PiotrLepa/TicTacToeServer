package com.piotr.tictactoe.core.security.error

import com.piotr.tictactoe.core.security.exception.SecurityUserNotExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator

class OAuth2ResponseExceptionTranslator : DefaultWebResponseExceptionTranslator() {

  @Autowired
  private lateinit var messageSource: MessageSource

  override fun translate(exception: Exception): ResponseEntity<OAuth2Exception> {
    val responseEntity = super.translate(exception)
    val customException = responseEntity.body?.apply {
      addAdditionalInformation("code", httpErrorCode.toString())
      addAdditionalInformation("developerMessage", this::class.simpleName.toString())
      getMessageCodeForException(this)?.let {
        addAdditionalInformation("printableMessage",
            messageSource.getMessage(it, null, LocaleContextHolder.getLocale()))
      }
    } ?: return responseEntity
    return ResponseEntity.status(customException.httpErrorCode)
        .body(customException)
  }

  private fun getMessageCodeForException(exception: OAuth2Exception): String? =
      if (exception is SecurityUserNotExistsException) {
        "security.error.user_not_exists"
      } else {
        null
      }
}