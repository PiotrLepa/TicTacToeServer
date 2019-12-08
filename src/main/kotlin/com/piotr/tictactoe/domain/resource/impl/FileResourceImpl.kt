package com.piotr.tictactoe.domain.resource.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.resource.FileResource
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class FileResourceImpl : FileResource {

  private val mapper = jacksonObjectMapper()

  override fun saveGame(game: GameDto) {
    File(DIRECTORY_PATH).mkdirs()
    val file = File(getFileName(game.gameId))
    mapper.writeValue(file, game)
  }

  override fun loadGame(gameId: Long): GameDto {
    val file = File(getFileName(gameId))
    return mapper.readValue(file)
  }

  private fun getFileName(name: Long) = "$DIRECTORY_PATH${File.separator}$name$FILE_EXTENSION"

  companion object {
    private val DIRECTORY_PATH = System.getProperty("user.dir") + File.separator + "TicTacToeSaves"
    private const val FILE_EXTENSION = ".json"
  }
}