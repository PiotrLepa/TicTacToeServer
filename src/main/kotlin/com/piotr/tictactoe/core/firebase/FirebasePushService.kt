package com.piotr.tictactoe.core.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.piotr.tictactoe.core.extensions.getLocalizedMessage
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
    val title = messageSource.getLocalizedMessage("push.game_request.title")
    val body = messageSource.getLocalizedMessage("push.game_request.body", opponent.username)
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