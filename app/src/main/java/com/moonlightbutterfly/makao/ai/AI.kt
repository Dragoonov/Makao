package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.*

class AI {
    fun getActionsForPlayer(player: Player, boardState: BoardState): Triple<BoardState, List<Action>, Player> {
        val clonedPlayer = player.copy(name = player.name, hand = player.hand)
        val clonedBoardState = boardState.clone()
        val actions = mutableListOf<Action>().apply {
            add(DrawCard(clonedPlayer, Card(CardValue.KING, CardType.SPADES)))
            add(DrawCard(clonedPlayer, Card(CardValue.TEN, CardType.HEARTS)))
            add(DrawCard(clonedPlayer, Card(CardValue.QUEEN, CardType.SPADES)))
            add(DrawCard(clonedPlayer, Card(CardValue.JACK, CardType.HEARTS)))
            add(DrawCard(clonedPlayer, Card(CardValue.NINE, CardType.SPADES)))
            add(DrawCard(clonedPlayer, Card(CardValue.EIGHT, CardType.HEARTS)))
            add(PlaceCard(clonedPlayer, Card(CardValue.QUEEN, CardType.SPADES)))
            add(PlaceCard(clonedPlayer, Card(CardValue.NINE, CardType.SPADES)))
        }
        // TODO write logic
        return Triple(clonedBoardState, actions, player)
    }
}