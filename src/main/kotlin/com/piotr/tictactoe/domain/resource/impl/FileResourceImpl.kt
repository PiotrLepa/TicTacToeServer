package com.piotr.tictactoe.domain.resource.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.resource.FileResource
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class FileResourceImpl : FileResource {

  private val mapper = ObjectMapper()

  override fun saveGame(game: GameDto) {
    File(DIRECTORY_PATH).mkdirs()
    mapper.writeValue(File(getFileName(game.gameId)), game)
  }

  private fun getFileName(name: Long) = "$DIRECTORY_PATH${File.separator}$name$FILE_EXTENSION"

  companion object {
    private val DIRECTORY_PATH = System.getProperty("user.dir") + File.separator + "TicTacToeSaves"
    private const val FILE_EXTENSION = ".json"
  }
}