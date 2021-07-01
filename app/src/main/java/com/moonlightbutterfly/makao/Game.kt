package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.ai.AIOutput
import com.moonlightbutterfly.makao.dataclasses.BoardState
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.Player
import com.moonlightbutterfly.makao.effect.*
import com.moonlightbutterfly.makao.utils.CardPeeker

class Game(playerNames: List<String>) {

    private val players = playerNames.map { Player(it, mutableListOf()) }
    private var currentPlayer = players[0]
    private val ai = AI { getEffectForCard(it) }
    private var boardState = BoardState()
    private var effectListener: ((Effect?) -> Unit)? = null

    fun startGame(): List<Action> {
        boardState = initializeBoardState()
        return initializeFirstCardDraws()
    }

    fun getTopCard() = boardState.topStack.last()

    fun getPlayerHand(playerName: String) = players.first { it.name == playerName }.hand

    fun setOnEffectListener(listener: (Effect?) -> Unit) {
        this.effectListener = listener
    }

    fun getCardsTakenInRound() = boardState.cardsTakenInRound

    fun getCardWasPlacedInRound() = boardState.cardPlacedInRound

    fun getCurrentEffect() = boardState.effect

    fun drawCard(playerName: String): List<Action> {
        boardState.cardsTakenInRound += 1
        val player = players.first { it.name == playerName }
        val card = boardState.deck.removeLast()
        if (boardState.deck.isEmpty()) {
            shuffle()
        }
        player.hand.add(card)
        return listOf(DrawCardAction(player, card))
    }

    fun placeCardOnTop(playerName: String, card: Card, effect: Effect? = null) {
        boardState.cardPlacedInRound = true
        boardState.topStack.add(card)
        val player = players.first { it.name == playerName }
        player.hand.remove(card)
        val cardEffect = effect ?: getEffectForCard(card)
        updateEffect(cardEffect)
    }

    fun nextTurn(skipPlayer: String? = null): List<Action> {
        if (currentPlayer.name == skipPlayer) {
            currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
            onTurnEnd()
            return emptyList()
        }
        val output = ai.getActionsForPlayer(currentPlayer.clone(), boardState.clone())
        updateStateFromAIBundle(output)
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        onTurnEnd()
        return output.actions
    }

    private fun updateStateFromAIBundle(output: AIOutput) {
        currentPlayer.hand = output.player.hand
        boardState = output.boardState
        effectListener?.let { it(boardState.effect) }
    }

    private fun onTurnEnd() {
        calculateEffectPersistence()
        boardState.cardsTakenInRound = 0
        boardState.cardPlacedInRound = false
    }

    private fun updateEffect(effect: Effect?) {
        if (boardState.effect == null) {
            boardState.effect = effect
        } else {
            boardState.effect = boardState.effect?.merge(effect)
        }
        effectListener?.let { it(boardState.effect) }
    }

    private fun initializeFirstCardDraws(): List<Action> {
        val actions = mutableListOf<Action>()
        for (player in players) {
            repeat(5) {
                val card = boardState.deck.removeLast()
                player.hand.add(card)
                actions.add(DrawCardAction(player, card))
            }
        }
        actions.add(InitializeCardAction(boardState.topStack.first()))
        return actions
    }

    private fun initializeBoardState(): BoardState {
        val boardState = BoardState()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                boardState.deck.add(Card(rank, suit))
            }
        }
        boardState.deck.shuffle()
        val topCard = boardState.deck.findLast { it.isActionCard().not() }!!
        boardState.topStack.add(topCard)
        boardState.deck.remove(topCard)
        return boardState
    }

    private fun shuffle() {
        boardState.deck.addAll(boardState.topStack.dropLast(1).shuffled())
        boardState.topStack.removeAll { it != boardState.topStack.last() }
    }

    private fun getEffectForCard(card: Card): Effect? {
        return when (card.rank) {
            Rank.TWO -> DrawCardsEffect(CARDS_DRAWN_FOR_TWO_CARD)
            Rank.THREE -> DrawCardsEffect(CARDS_DRAWN_FOR_THREE_CARD)
            Rank.FOUR -> WaitTurnEffect(TURNS_WAIT_FOR_FOUR_CARD)
            else -> {
                if (card in listOf(CardPeeker.KING_OF_HEARTS, CardPeeker.KING_OF_SPADES)) {
                    DrawCardsEffect(CARDS_DRAWN_FOR_KING_CARD)
                } else {
                    null
                }
            }
        }
    }

    private fun calculateEffectPersistence() {
        val conditionForDrawCards = boardState.effect is DrawCardsEffect && boardState.cardsTakenInRound >= (boardState.effect as DrawCardsEffect).getCardsAmount()
        val conditionForWaitTurn = boardState.effect is WaitTurnEffect && (boardState.effect as WaitTurnEffect).getTurnsNumber() <= 0
        if (conditionForDrawCards or conditionForWaitTurn) {
            boardState.effect = null
        } else if (boardState.effect is WaitTurnEffect) {
            (boardState.effect as WaitTurnEffect).decreaseTurn()
        }
    }

    private fun Card.isActionCard() = this.rank in arrayOf(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)

    private companion object {
        private const val CARDS_DRAWN_FOR_THREE_CARD = 3
        private const val CARDS_DRAWN_FOR_TWO_CARD = 2
        private const val CARDS_DRAWN_FOR_KING_CARD = 5
        private const val TURNS_WAIT_FOR_FOUR_CARD = 1
    }
}