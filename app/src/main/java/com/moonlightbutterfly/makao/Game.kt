package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.ai.AIInputBundle

class Game(private val players: List<Player>) {
    private var currentPlayer = players[0]
    private val ai = AI()
    private val stack: MutableList<Card> = mutableListOf()
    private val sideStack: MutableList<Card> = mutableListOf()
    private val effects: MutableList<Card> = mutableListOf()

    fun nextTurn(): Pair<Player, List<Action>> {
        val output = ai.getActionsForPlayer(AIInputBundle(currentPlayer, stack, sideStack, effects))
        currentPlayer.hand = output.bundle.player.hand
        stack.apply {
            clear()
            addAll(output.bundle.stack)
        }
        sideStack.apply {
            clear()
            addAll(output.bundle.sideStack)
        }
        effects.apply {
            clear()
            addAll(output.bundle.effects)
        }
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        return output.bundle.player to output.actions
    }
}