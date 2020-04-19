package com.piotr.tictactoe.core.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class FirebaseInitializer @Autowired constructor(
  @Value("\${app.firebase-configuration-file}")
  private val firebaseConfigPath: String
) {

  @PostConstruct
  fun initialize() {
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath).inputStream))
        .setDatabaseUrl("https://tic-tac-toe-67105.firebaseio.com")
        .build()
    FirebaseApp.initializeApp(options)
  }
}