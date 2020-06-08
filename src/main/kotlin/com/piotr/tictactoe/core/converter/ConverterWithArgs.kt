package com.piotr.tictactoe.core.converter

interface ConverterWithArgs<T, S, P1> {

  fun convert(from: T, args: P1): S
}