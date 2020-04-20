package com.piotr.tictactoe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableConfigurationProperties
@EnableAsync
@SpringBootApplication
class TicTacToeApplication

fun main(args: Array<String>) {
  runApplication<TicTacToeApplication>(*args)
}