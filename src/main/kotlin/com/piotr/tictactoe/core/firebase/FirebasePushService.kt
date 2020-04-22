package com.piotr.tictactoe.core.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebasePushService @Autowired constructor(
  private val firebaseMessaging: FirebaseMessaging
) {

  fun sendGameInvitation(token: String, gameDto: MultiplayerGameCreatedDto) {
    val message = Message.builder()
        .setToken(token)
        .putData("gameId", gameDto.gameId.toString())
        .putData("yourMark", gameDto.yourMark.toString())
        .putData("playerType", gameDto.playerType.toString())
        .build()
    firebaseMessaging.send(message)
  }
}