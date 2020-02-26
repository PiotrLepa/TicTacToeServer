package com.piotr.tictactoe.core.security.error

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator

class OAuth2ResponseExceptionTranslator : DefaultWebResponseExceptionTranslator() {

  override fun translate(exception: Exception): ResponseEntity<OAuth2Exception> {
    val responseEntity = super.translate(exception)
    val customException = responseEntity.body?.apply {
      addAdditionalInformation("code", httpErrorCode.toString())
      addAdditionalInformation("exception", this::class.simpleName.toString())
    } ?: return responseEntity
    return ResponseEntity.status(customException.httpErrorCode)
        .body(customException)
  }
}