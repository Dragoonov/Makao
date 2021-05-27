package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.effect.*


class CardsHighlighter {

    fun provideCardsToHighlight(
        hand: List<Card>,
        topCard: Card,
        cardWasTaken: Boolean,
        cardWasPlaced: Boolean,
        effect: Effect? = null
    ): Pair<List<Card>, Boolean> {
        if (topCard == CardPeeker.QUEEN_OF_SPADES) {
            return hand.toList() to (cardWasTaken or cardWasPlaced).not()
        }
        return if (effect == null) {
            provideCardsForNoEffect(hand, topCard, cardWasTaken, cardWasPlaced)
        } else {
            when (effect) {
                is DrawCardsEffect -> provideCardsForDrawCardsEffect(hand, topCard, cardWasTaken, cardWasPlaced)
                is RequireSuitEffect -> provideCardsForRequireSuitEffect(hand, topCard, effect, cardWasTaken, cardWasPlaced)
                is RequireRankEffect -> provideCardsForRequireRankEffect(hand, topCard, effect, cardWasTaken, cardWasPlaced)
                is WaitTurnEffect -> provideCardsForWaitTurnEffect(hand, cardWasTaken)
                else -> error("Wrong effect: $effect")
            }
        }
    }

    private fun provideCardsForNoEffect(
        hand: List<Card>,
        topCard: Card,
        cardWasTaken: Boolean,
        cardWasPlaced: Boolean
    ): Pair<List<Card>, Boolean> {
        return hand.filter {
            if (cardWasPlaced) {
                it.rank == topCard.rank
            } else {
                it.rank == topCard.rank || it.suit == topCard.suit || (it == CardPeeker.QUEEN_OF_SPADES)
            }
        } to (cardWasTaken or cardWasPlaced).not()
    }

    private fun provideCardsForDrawCardsEffect(hand: List<Card>, topCard: Card, cardWasTaken: Boolean, cardWasPlaced: Boolean): Pair<List<Card>, Boolean> {
        return hand.filter {
            if (cardWasPlaced) {
                it.rank == topCard.rank
            } else {
                it.rank in listOf(Rank.TWO, Rank.THREE) || it in listOf(
                    CardPeeker.QUEEN_OF_SPADES,
                    CardPeeker.KING_OF_HEARTS,
                    CardPeeker.KING_OF_SPADES
                )
            }
        } to (cardWasTaken or cardWasPlaced).not()
    }

    private fun provideCardsForRequireRankEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireRankEffect,
        cardWasTaken: Boolean,
        cardWasPlaced: Boolean
    ): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.rank == effect.getRank() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
        } to (cardWasTaken or cardWasPlaced).not()
    }

    private fun provideCardsForRequireSuitEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireSuitEffect,
        cardWasTaken: Boolean,
        cardWasPlaced: Boolean
    ): Pair<List<Card>, Boolean> {
        return hand.filter {
            it.suit == effect.getSuit() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
        } to (cardWasTaken or cardWasPlaced).not()
    }

    private fun provideCardsForWaitTurnEffect(hand: List<Card>, cardWasTaken: Boolean): Pair<List<Card>, Boolean> {
        return hand.filter { it.rank == Rank.FOUR } to cardWasTaken.not()
    }
}