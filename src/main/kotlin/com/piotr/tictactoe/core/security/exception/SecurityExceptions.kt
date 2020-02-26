package com.piotr.tictactoe.core.security.exception

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

class SecurityUserNotExistsException(override val message: String? = null) : OAuth2Exception(message)