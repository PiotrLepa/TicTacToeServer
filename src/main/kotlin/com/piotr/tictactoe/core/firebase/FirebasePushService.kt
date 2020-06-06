package com.piotr.tictactoe.core.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.piotr.tictactoe.core.extensions.getMessageForTag
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
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
    val title = messageSource.getMessageForTag("push.game_request.title", opponent.languageTag)
    val body = messageSource.getMessageForTag("push.game_request.body", opponent.languageTag, opponent.username)
    val message = Message.builder()
        .setToken(token)
        .setNotification(Notification(title, body))
        .putAllData(mapOf(
            "gameId" to gameDto.gameId.toString(),
            "socketDestination" to gameDto.socketDestination,
            "yourMark" to gameDto.yourMark.toString(),
            "playerType" to gameDto.playerType.toString(),
            "body" to body,
            getFlutterClickAction()
        ))
        .build()
    firebaseMessaging.send(message)
  }

  private fun getFlutterClickAction(): Pair<String, String> =
      FLUTTER_CLICK_ACTION_KEY to FLUTTER_CLICK_ACTION_VALUE

  companion object {
    private const val FLUTTER_CLICK_ACTION_KEY = "click_action"
    private const val FLUTTER_CLICK_ACTION_VALUE = "FLUTTER_NOTIFICATION_CLICK"
  }
}