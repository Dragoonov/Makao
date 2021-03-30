package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Card
import com.moonlightbutterfly.makao.Player

data class AIInputBundle (
    val player: Player,
    val stack: List<Card>,
    val sideStack: List<Card>,
    val effects: List<Card>
)

fun AIInputBundle.clone(): AIInputBundle {
    return AIInputBundle(
        this.player.copy(name = player.name, hand = player.hand),
        stack.take(stack.size),
        sideStack.take(sideStack.size),
        effects.take(effects.size)
    )
}