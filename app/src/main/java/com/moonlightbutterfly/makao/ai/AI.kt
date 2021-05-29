package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.*

class AI {
    fun getActionsForPlayer(player: Player, boardState: BoardState): Triple<BoardState, List<Action>, Player> {
        val clonedPlayer = player.copy(name = player.name, hand = player.hand)
        val clonedBoardState = boardState.clone()
        val card = player.hand.removeLast()
        val cardToTake = clonedBoardState.deck.removeLast()
        player.hand.add(cardToTake)
        clonedBoardState.topStack += card
        val actions = mutableListOf<Action>().apply {
            add(DrawCardAction(clonedPlayer, cardToTake))
            add(PlaceCardAction(clonedPlayer, card))
        }
        return Triple(clonedBoardState, actions, player)
    }
}