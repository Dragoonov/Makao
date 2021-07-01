package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.dataclasses.BoardState
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.Player
import com.moonlightbutterfly.makao.effect.Effect
import com.moonlightbutterfly.makao.effect.RequireRankEffect
import com.moonlightbutterfly.makao.effect.RequireSuitEffect
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter

class AI(private val effectProvider: (Card) -> Effect?) {

    private val highlighter = OptionsHighlighter.instance
    var cardsTakenInRound = 0
    var cardPlacedInRound = false

    fun getActionsForPlayer(player: Player, boardState: BoardState): AIOutput {
        val actions = mutableListOf<Action>()
        cardsTakenInRound = 0
        cardPlacedInRound = false
        var finished = false
        while (!finished) {
            val results = highlighter.provideOptionsToHighlight(
                player.hand,
                boardState.topStack.last(),
                cardsTakenInRound,
                cardPlacedInRound,
                boardState.effect
            )
            when {
                results.cardsToPlay.isNotEmpty() -> {
                    val action = calculateCardToPlay(results.cardsToPlay, boardState, player)
                    actions.add(action)
                }

                results.finishRoundPossible -> {
                    finished = true
                }

                results.drawPossible -> {
                    val action = drawCard(boardState, player)
                    actions.add(action)
                }
            }
            boardState.cardsTakenInRound = cardsTakenInRound
            boardState.cardPlacedInRound = cardPlacedInRound
        }
        return AIOutput(boardState, actions, player)
    }

    private fun drawCard(boardState: BoardState, player: Player): Action {
        val card = boardState.deck.removeLast()
        player.hand.add(card)
        cardsTakenInRound += 1
        if (boardState.deck.isEmpty()) {
            shuffle(boardState.deck, boardState.topStack)
        }
        return DrawCardAction(player, card)
    }

    private fun updateBoardEffect(boardState: BoardState, effect: Effect?) {
        if (boardState.effect == null) {
            boardState.effect = effect
        } else {
            boardState.effect = boardState.effect?.merge(effect)
        }
    }

    private fun calculateCardToPlay(cardsToPlay: List<Card>, boardState: BoardState, player: Player): Action{
        val cardToPlay = cardsToPlay.random()
        player.hand.remove(cardToPlay)
        boardState.topStack += cardToPlay
        cardPlacedInRound = true
        val cardEffect = calculateCardEffect(cardToPlay)
        updateBoardEffect(boardState, cardEffect)
        return PlaceCardAction(player, cardToPlay)
    }

    private fun calculateCardEffect(cardToPlay: Card): Effect? {
        return when (cardToPlay.rank) {
            Rank.ACE -> {
                RequireSuitEffect(Suit.values().random())
            }
            Rank.JACK -> {
                RequireRankEffect(arrayOf(Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN).random())
            }
            else -> {
                effectProvider(cardToPlay)
            }
        }
    }

    private fun shuffle(deck: MutableList<Card>, topStack: MutableList<Card>) {
        deck.addAll(topStack.dropLast(1).shuffled())
        topStack.removeAll { it != topStack.last() }
    }
}