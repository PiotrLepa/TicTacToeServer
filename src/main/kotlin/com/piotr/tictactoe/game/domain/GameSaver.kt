package com.piotr.tictactoe.game.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.piotr.tictactoe.game.dto.GameDto
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class GameSaver {

  private val mapper = jacksonObjectMapper()

  fun saveGame(game: GameDto) {
    File(DIRECTORY_PATH).mkdirs()
    val file = File(getFileName(game.gameId))
    mapper.writeValue(file, game)
  }

  fun loadGame(gameId: Long): GameDto {
    val file = File(getFileName(gameId))
    return mapper.readValue(file)
  }

  private fun getFileName(name: Long) = "$DIRECTORY_PATH${File.separator}$name$FILE_EXTENSION"

  companion object {
    private val DIRECTORY_PATH = System.getProperty("user.dir") + File.separator + "TicTacToeSaves"
    private const val FILE_EXTENSION = ".json"
  }
}