package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.*

class AI {
    fun getActionsForPlayer(player: Player, boardState: BoardState): Triple<BoardState, List<Action>, Player> {
        val clonedPlayer = player.copy(name = player.name, hand = player.hand)
        val clonedBoardState = boardState.clone()
        val actions = mutableListOf<Action>().apply {
            add(DrawCardAction(clonedPlayer, Card(Rank.KING, Suit.SPADES)))
            add(DrawCardAction(clonedPlayer, Card(Rank.TEN, Suit.HEARTS)))
            add(DrawCardAction(clonedPlayer, Card(Rank.QUEEN, Suit.SPADES)))
            add(DrawCardAction(clonedPlayer, Card(Rank.JACK, Suit.HEARTS)))
            add(DrawCardAction(clonedPlayer, Card(Rank.NINE, Suit.SPADES)))
            add(DrawCardAction(clonedPlayer, Card(Rank.EIGHT, Suit.HEARTS)))
            add(PlaceCardAction(clonedPlayer, Card(Rank.QUEEN, Suit.SPADES)))
            add(PlaceCardAction(clonedPlayer, Card(Rank.NINE, Suit.SPADES)))
        }
        // TODO write logic
        return Triple(clonedBoardState, actions, player)
    }
}