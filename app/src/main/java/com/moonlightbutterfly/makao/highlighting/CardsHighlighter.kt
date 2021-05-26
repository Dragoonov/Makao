package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.effect.*


class CardsHighlighter {

    fun provideCardsToHighlight(hand: List<Card>, topCard: Card, cardWasTaken: Boolean, effect: Effect? = null): Pair<List<Card>, Boolean> {
        if (topCard == CardPeeker.QUEEN_OF_SPADES) {
            return hand.toList() to cardWasTaken.not()
        }
        return if (effect == null) {
            provideCardsForNoEffect(hand, topCard, cardWasTaken)
        } else {
            when (effect) {
                is DrawCardsEffect -> provideCardsForDrawCardsEffect(hand, topCard)
                is RequireSuitEffect -> provideCardsForRequireSuitEffect(hand, topCard, effect, cardWasTaken)
                is RequireRankEffect -> provideCardsForRequireRankEffect(hand, topCard, effect, cardWasTaken)
                is WaitTurnEffect -> provideCardsForWaitTurnEffect(hand, cardWasTaken)
                else -> error("Wrong effect: $effect")
            }
        }
    }

    private fun provideCardsForNoEffect(hand: List<Card>, topCard: Card, cardWasTaken: Boolean): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.rank == topCard.rank || it.suit == topCard.suit || (it == CardPeeker.QUEEN_OF_SPADES)
        } to cardWasTaken.not()
    }

    private fun provideCardsForDrawCardsEffect(hand: List<Card>, topCard: Card): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.rank == topCard.rank || it.suit == topCard.suit || it == CardPeeker.QUEEN_OF_SPADES
        } to true
    }

    private fun provideCardsForRequireRankEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireRankEffect,
        cardWasTaken: Boolean
    ): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.rank == effect.getRank() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
        } to cardWasTaken.not()
    }

    private fun provideCardsForRequireSuitEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireSuitEffect,
        cardWasTaken: Boolean
    ): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.suit == effect.getSuit() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
        } to cardWasTaken.not()
    }

    private fun provideCardsForWaitTurnEffect(hand: List<Card>, cardWasTaken: Boolean): Pair<List<Card>, Boolean> {
        return hand.filter { it.rank == CardPeeker.FOUR_OF_SPADES.rank } to cardWasTaken.not()
    }
}