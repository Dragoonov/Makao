package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.effect.*


class OptionsHighlighter {

    fun provideOptionsToHighlight(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        effect: Effect? = null
    ): Triple<List<Card>, Boolean, Boolean> {
        if (topCard == CardPeeker.QUEEN_OF_SPADES) {
            return Triple(
                if (cardWasPlaced) {
                    emptyList()
                } else {
                    hand.toList()
                },
                ((cardsTakenInRound >= 1) or cardWasPlaced).not(),
                (cardWasPlaced or (cardsTakenInRound >= 1))
            )
        }
        return if (effect == null) {
            provideCardsForNoEffect(hand, topCard, cardsTakenInRound, cardWasPlaced)
        } else {
            when (effect) {
                is DrawCardsEffect -> provideCardsForDrawCardsEffect(
                    hand,
                    topCard,
                    cardsTakenInRound,
                    cardWasPlaced,
                    effect.getCardsAmount()
                )
                is RequireSuitEffect -> provideCardsForRequireSuitEffect(hand, topCard, effect, cardsTakenInRound, cardWasPlaced)
                is RequireRankEffect -> provideCardsForRequireRankEffect(hand, topCard, effect, cardsTakenInRound, cardWasPlaced)
                is WaitTurnEffect -> provideCardsForWaitTurnEffect(hand, cardsTakenInRound)
                else -> error("Wrong effect: $effect")
            }
        }
    }

    private fun provideCardsForNoEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): Triple<List<Card>, Boolean, Boolean> {
        return Triple(hand.filter {
            if (cardWasPlaced) {
                it.rank == topCard.rank
            } else {
                it.rank == topCard.rank || it.suit == topCard.suit || (it == CardPeeker.QUEEN_OF_SPADES)
            }
        }, ((cardsTakenInRound >= 1) or cardWasPlaced).not(), ((cardsTakenInRound >= 1) or cardWasPlaced))
    }

    private fun provideCardsForDrawCardsEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        cardsAmount: Int
    ): Triple<List<Card>, Boolean, Boolean> {
        return Triple(hand.filter {
            if (cardWasPlaced) {
                it.rank == topCard.rank
            } else {
                it.rank in listOf(Rank.TWO, Rank.THREE) || it in listOf(
                    CardPeeker.QUEEN_OF_SPADES,
                    CardPeeker.KING_OF_HEARTS,
                    CardPeeker.KING_OF_SPADES
                )
            }
        }, ((cardsTakenInRound >= 1) or cardWasPlaced).not(), cardWasPlaced or (cardsTakenInRound >= cardsAmount))
    }

    private fun provideCardsForRequireRankEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireRankEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): Triple<List<Card>, Boolean, Boolean> {
        return Triple(hand.filter {
            if (cardWasPlaced.not()) {
                it.rank == effect.getRank() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
            } else {
                it.rank == topCard.rank
            }
        }, ((cardsTakenInRound >= 1) or cardWasPlaced).not(), (cardsTakenInRound >= 1))
    }

    private fun provideCardsForRequireSuitEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireSuitEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): Triple<List<Card>, Boolean, Boolean> {
        return Triple(hand.filter {
            if (cardWasPlaced.not()) {
                it.suit == effect.getSuit() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
            } else {
                it.rank == topCard.rank
            }
        }, ((cardsTakenInRound >= 1) or cardWasPlaced).not(), cardWasPlaced)
    }

    private fun provideCardsForWaitTurnEffect(hand: List<Card>, cardsTakenInRound: Int): Triple<List<Card>, Boolean, Boolean> {
        return Triple(hand.filter { it.rank == Rank.FOUR }, cardsTakenInRound == 0, cardsTakenInRound >= 1)
    }
}