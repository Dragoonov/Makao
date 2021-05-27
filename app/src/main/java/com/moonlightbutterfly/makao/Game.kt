package com.moonlightbutterfly.makao

import android.util.Log
import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.effect.*

class Game(private val players: List<Player>) {
    private var currentPlayer = players[0]
    private val ai = AI()
    private val boardState = BoardState()

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

    fun drawCard(playerName: String): List<Action> {
        val player = players.find { it.name == playerName }!!
        val card = boardState.deck.removeFirst()
        player.hand.add(card)
        return listOf(DrawCardAction(player, card))
    }

    fun placeCardOnTop(playerName: String, card: Card, effect: Effect?) {
        val player = players.find { it.name == playerName }!!
        boardState.topStack.add(card)
        if (!player.hand.remove(card)) {
            Log.v(javaClass.simpleName, "Didn't find the $card in $player hand: ${player.hand}")
        }
        effect?.let {
            boardState.effect?.merge(it)
        }
    }

    fun nextTurn(skipPlayer: String? = null): List<Action> {
        skipPlayer?.let {
            if (currentPlayer.name == skipPlayer) {
                currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
            }
        }
        val output = ai.getActionsForPlayer(currentPlayer, boardState)
        currentPlayer.hand = output.third.hand
        boardState.deck.apply {
            clear()
            addAll(output.first.deck)
        }
        boardState.topStack.apply {
            clear()
            addAll(output.first.topStack)
        }
        boardState.effect = output.first.effect
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        return output.second
    }

    fun getTopCard() = boardState.topStack.last()

    private fun Card.isActionCard() =this.rank in arrayOf(Rank.TWO, Rank.THREE, Rank.FOUR, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)
}