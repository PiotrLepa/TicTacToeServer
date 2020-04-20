package com.piotr.tictactoe.utils

import org.springframework.stereotype.Component

@Component
class PlayerCodeGenerator {

  fun generate(): String = (MIN_CODE_VALUE..MAX_CODE_VALUE).random().toString()

  companion object {
    private const val MIN_CODE_VALUE = 10000000
    private const val MAX_CODE_VALUE = 99999999
  }
}