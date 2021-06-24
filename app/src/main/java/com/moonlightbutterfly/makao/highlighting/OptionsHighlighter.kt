package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.effect.*


class OptionsHighlighter private constructor() {

    fun provideOptionsToHighlight(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        effect: Effect? = null
    ): HighlightInfo {
        if (topCard.rank == Rank.QUEEN) {
            return HighlightInfo(
                if (cardWasPlaced) {
                    emptyList()
                } else {
                    hand.toList()
                },
                ((cardsTakenInRound >= ONE) or cardWasPlaced).not(),
                (cardWasPlaced or (cardsTakenInRound >= ONE))
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
                is WaitTurnEffect -> provideCardsForWaitTurnEffect(hand, cardsTakenInRound, cardWasPlaced)
                else -> error("Wrong effect: $effect")
            }
        }
    }

    private fun provideCardsForNoEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(hand.filter {
            if (cardWasPlaced) {
                it.rank == topCard.rank
            } else {
                it.rank == topCard.rank || it.suit == topCard.suit || (it == CardPeeker.QUEEN_OF_SPADES)
            }
        }, ((cardsTakenInRound >= ONE) or cardWasPlaced).not(), ((cardsTakenInRound >= ONE) or cardWasPlaced))
    }

    private fun provideCardsForDrawCardsEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        cardsAmount: Int
    ): HighlightInfo {
        return HighlightInfo(
            if (cardsTakenInRound > ONE) {
                emptyList()
            } else {
            hand.filter {
                if (cardWasPlaced) {
                    it.rank == topCard.rank
                } else {
                    it.rank in listOf(Rank.TWO, Rank.THREE) || it in listOf(
                        CardPeeker.QUEEN_OF_SPADES,
                        CardPeeker.KING_OF_HEARTS,
                        CardPeeker.KING_OF_SPADES
                    )
                }
            }
        }, ((cardsTakenInRound >= cardsAmount) or cardWasPlaced).not(), cardWasPlaced or (cardsTakenInRound >= cardsAmount))
    }

    private fun provideCardsForRequireRankEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireRankEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(hand.filter {
            if (cardWasPlaced.not()) {
                it.rank == effect.getRank() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
            } else {
                it.rank == topCard.rank
            }
        }, ((cardsTakenInRound >= ONE) or cardWasPlaced).not(), (cardsTakenInRound >= ONE) or cardWasPlaced)
    }

    private fun provideCardsForRequireSuitEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireSuitEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(hand.filter {
            if (cardWasPlaced.not()) {
                it.suit == effect.getSuit() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
            } else {
                it.rank == topCard.rank
            }
        }, ((cardsTakenInRound >= ONE) or cardWasPlaced).not(), cardWasPlaced or (cardsTakenInRound >= ONE))
    }

    private fun provideCardsForWaitTurnEffect(hand: List<Card>, cardsTakenInRound: Int, cardWasPlaced: Boolean): HighlightInfo {
        return HighlightInfo(
            hand.filter { it.rank == Rank.FOUR },
            (cardWasPlaced or (cardsTakenInRound >= ONE)).not(),
            (cardsTakenInRound >= ONE) or cardWasPlaced
        )
    }

    companion object {
        val instance = OptionsHighlighter()
        private const val ONE = 1
    }
}