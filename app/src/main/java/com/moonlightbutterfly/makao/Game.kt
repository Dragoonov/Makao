package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.ai.AIInputBundle

class Game(private val players: List<Player>) {
    private var currentPlayer = players[0]
    private val ai = AI()
    private val boardState = BoardState()

    fun nextTurn(): List<Action> {
        val output = ai.getActionsForPlayer(currentPlayer, boardState)
        currentPlayer.hand = output.third.hand
        boardState.stack.apply {
            clear()
            addAll(output.first.stack)
        }
        boardState.sideStack.apply {
            clear()
            addAll(output.first.sideStack)
        }
        boardState.effects.apply {
            clear()
            addAll(output.first.effects)
        }
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        return output.second
    }
}