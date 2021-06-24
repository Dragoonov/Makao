package com.moonlightbutterfly.makao

import android.util.Log
import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.effect.*

class Game(private val players: List<Player>) {
    private var currentPlayer = players[0]
    private val ai = AI { getEffectForCard(it) }
    private var boardState = BoardState()
    private var effectListener: ((Effect?) -> Unit)? = null

    fun startGame(): List<Action> {
        val actions = mutableListOf<Action>()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                boardState.deck.add(Card(rank, suit))
            }
        }
        boardState.deck.shuffle()
        for (player in players) {
            repeat(5) {
                val card = boardState.deck.removeFirst()
                player.hand.add(card)
                actions.add(DrawCardAction(player, card))
            }
        }
        val topCard = boardState.deck.findLast { it.isActionCard().not() }!!
        boardState.topStack.add(topCard)
        actions.add(InitializeCardAction(topCard))
        return actions
    }

    fun setOnEffectListener(listener: (Effect?) -> Unit) {
        this.effectListener = listener
    }

    fun getCardsTakenInRound() = boardState.cardsTakenInRound
    fun getCardWasPlacedInRound() = boardState.cardPlacedInRound

    fun getCurrentEffect() = boardState.effect

    fun drawCard(playerName: String): List<Action> {
        boardState.cardsTakenInRound += 1
        val player = players.find { it.name == playerName }!!
        val card = boardState.deck.removeFirst()
        if (boardState.deck.isEmpty()) {
            shuffle()
        }
        player.hand.add(card)
        return listOf(DrawCardAction(player, card))
    }

    private fun shuffle() {
        boardState.deck.addAll(boardState.topStack.dropLast(1).shuffled())
        boardState.topStack.removeAll { it != boardState.topStack.last() }
    }

    private fun getEffectForCard(card: Card): Effect? {
        return when (card.rank) {
            Rank.TWO -> DrawCardsEffect(2)
            Rank.THREE -> DrawCardsEffect(3)
            Rank.FOUR -> WaitTurnEffect(1)
            else -> {
                if (card.rank == Rank.KING && (card.suit == Suit.SPADES || card.suit == Suit.HEARTS)) {
                    DrawCardsEffect(5)
                } else {
                    null
                }
            }
        }
    }

    fun placeCardOnTop(playerName: String, card: Card, effect: Effect? = null) {
        boardState.cardPlacedInRound = true
        val player = players.find { it.name == playerName }!!
        boardState.topStack.add(card)
        if (!player.hand.remove(card)) {
            Log.v(javaClass.simpleName, "Didn't find the $card in $player hand: ${player.hand}")
        }
        val cardEffect = effect ?: getEffectForCard(card)
        if (boardState.effect == null) {
            boardState.effect = cardEffect
        } else {
            boardState.effect = boardState.effect?.merge(cardEffect)
        }
        effectListener?.let { it(boardState.effect) }
    }

    fun onTurnEnd() {
        calculateEffectPersistence()
        boardState.cardsTakenInRound = 0
        boardState.cardPlacedInRound = false
    }

    fun nextTurn(skipPlayer: String? = null): List<Action> {
        skipPlayer?.let {
            if (currentPlayer.name == skipPlayer) {
                currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
            }
        }
        val output = ai.getActionsForPlayer(currentPlayer, boardState)
        currentPlayer.hand = output.third.hand
        boardState = output.first
        effectListener?.let { it(boardState.effect) }
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        onTurnEnd()
        return output.second
    }

    private fun calculateEffectPersistence() {
        val conditionForDrawCards = boardState.effect is DrawCardsEffect && boardState.cardsTakenInRound >= (boardState.effect as DrawCardsEffect).getCardsAmount()
        val conditionForWaitTurn = boardState.effect is WaitTurnEffect
        if (conditionForDrawCards or conditionForWaitTurn) {
            boardState.effect = null
        }
    }

    fun getTopCard() = boardState.topStack.last()

    private fun Card.isActionCard() = this.rank in arrayOf(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)
}