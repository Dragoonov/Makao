package com.moonlightbutterfly.makao

data class PlayerAction (
    val player: Player,
    val actions: Map<Action, Execution>
)

enum class Action {
    DRAW_CARD, POST_CARD, WIN
}

@FunctionalInterface
interface Execution {
    fun execute()
}

interface CardExecution: Execution {
    var card: Card
    fun provideCard(card: Card) {
        this.card = card
    }
}