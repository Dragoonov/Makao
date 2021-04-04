package com.moonlightbutterfly.makao

data class BoardState (
    val stack: MutableList<Card> = mutableListOf(),
    val sideStack: MutableList<Card> = mutableListOf(),
    val effects: MutableList<Card> = mutableListOf()
)

fun BoardState.clone(): BoardState = BoardState(
    stack.take(stack.size).toMutableList(),
    sideStack.take(sideStack.size).toMutableList(),
    effects.take(effects.size).toMutableList()
)