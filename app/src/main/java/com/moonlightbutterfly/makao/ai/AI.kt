package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.effect.Effect
import com.moonlightbutterfly.makao.effect.RequireRankEffect
import com.moonlightbutterfly.makao.effect.RequireSuitEffect
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter

class AI(private val effectProvider: (Card) -> Effect?) {

    private val highlighter = OptionsHighlighter.instance

    fun getActionsForPlayer(player: Player, boardState: BoardState): Triple<BoardState, List<Action>, Player> {
        val clonedPlayer = player.copy(name = player.name, hand = player.hand)
        val clonedBoardState = boardState.clone()
        val actions = mutableListOf<Action>()
        var cardsTakenInRound = 0
        var cardPlacedInRound = false
        var finished = false
        while (!finished) {
            val results = highlighter.provideOptionsToHighlight(
                clonedPlayer.hand,
                clonedBoardState.topStack.last(),
                cardsTakenInRound,
                cardPlacedInRound,
                clonedBoardState.effect)
            val cardsToPlay = results.cardsToPlay
            val canTakeCard = results.drawPossible
            val canFinish = results.finishRoundPossible
            when {
                cardsToPlay.isNotEmpty() -> {
                    val cardToPlay = cardsToPlay.random()
                    clonedPlayer.hand.remove(cardToPlay)
                    clonedBoardState.topStack += cardToPlay
                    actions.add(PlaceCardAction(clonedPlayer, cardToPlay))
                    cardPlacedInRound = true
                    var cardEffect = effectProvider(cardToPlay)
                    if (cardToPlay.rank == Rank.ACE) {
                        cardEffect = RequireSuitEffect(Suit.values().random())
                    } else if (cardToPlay.rank == Rank.JACK) {
                        cardEffect = RequireRankEffect(arrayOf(Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN).random())
                    }
                    if (clonedBoardState.effect == null) {
                        clonedBoardState.effect = cardEffect
                    } else {
                        clonedBoardState.effect = clonedBoardState.effect?.merge(cardEffect)
                    }
                }
                canFinish -> {
                    finished = true
                }
                canTakeCard -> {
                    val card = clonedBoardState.deck.removeLast()
                    clonedPlayer.hand.add(card)
                    actions.add(DrawCardAction(clonedPlayer, card))
                    cardsTakenInRound += 1
                    if (clonedBoardState.deck.isEmpty()) {
                        shuffle(clonedBoardState.deck, clonedBoardState.topStack)
                    }
                }
            }
            clonedBoardState.cardsTakenInRound = cardsTakenInRound
            clonedBoardState.cardPlacedInRound = cardPlacedInRound
        }
        return Triple(clonedBoardState, actions, clonedPlayer)
    }

    private fun shuffle(deck: MutableList<Card>, topStack: MutableList<Card>) {
        deck.addAll(topStack.dropLast(1).shuffled())
        topStack.removeAll { it != topStack.last() }
    }
}