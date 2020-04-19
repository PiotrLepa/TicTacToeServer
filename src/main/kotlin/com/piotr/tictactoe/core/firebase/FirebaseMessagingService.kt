package com.piotr.tictactoe.core.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingService {

  fun send(token: String) {
    val message = Message.builder()
        .setToken(token)
        .putData("gameId", "1231231")
        .build()
    FirebaseMessaging.getInstance().send(message)
  }
}