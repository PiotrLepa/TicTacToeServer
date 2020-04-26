package com.piotr.tictactoe.core.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class FirebasePushService @Autowired constructor(
  private val firebaseMessaging: FirebaseMessaging,
  private val messageSource: MessageSource
) {

  fun sendGameInvitation(
    token: String,
    gameDto: MultiplayerGameCreatedDto,
    opponent: UserDto
  ) {
    val message = Message.builder()
        .setToken(token)
        .putAllData(mapOf(
            "title" to messageSource.getMessage("push.game_request.title", null, LocaleContextHolder.getLocale()),
            "body" to messageSource.getMessage("push.game_request.body", arrayOf(opponent.username), LocaleContextHolder.getLocale()),
            "gameId" to gameDto.gameId.toString(),
            "yourMark" to gameDto.yourMark.toString(),
            "playerType" to gameDto.playerType.toString()
        ))
        .build()
    firebaseMessaging.send(message)
  }
}