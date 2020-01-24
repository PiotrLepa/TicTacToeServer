package com.piotr.tictactoe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class TicTacToeApplication

fun main(args: Array<String>) {
  runApplication<TicTacToeApplication>(*args)
}
