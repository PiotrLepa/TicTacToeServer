package com.piotr.tictactoe.core.converter

interface Converter<T, S> {

  fun convert(from: T): S
}