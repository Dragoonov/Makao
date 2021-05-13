package com.moonlightbutterfly.makao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.makao.highlighting.CardsHighlighter

class GameViewModel : ViewModel() {

    private val john = Player(JOHN, mutableListOf())
    private val arthur = Player(ARTHUR, mutableListOf())
    private val mainPlayer = Player(MAIN_PLAYER, mutableListOf())
    private val game = Game(listOf(john, arthur, mainPlayer))
    private val cardsHighlighter = CardsHighlighter()
    private var cardWasTakenInRound = false

    private val _possibleMoves = MutableLiveData<List<Card>>()
    val possibleMoves: LiveData<List<Card>> = _possibleMoves

    private val _drawPossible = MutableLiveData<Boolean>()
    val drawPossible: LiveData<Boolean> = _drawPossible

    private val _johnWon = MutableLiveData<Boolean>()
    val johnWon: LiveData<Boolean> = _johnWon

    private val _arthurWon = MutableLiveData<Boolean>()
    val arthurWon: LiveData<Boolean> = _arthurWon

    fun onCardPlacedOnTop(card: Card) {
        game.placeCardOnTop(MAIN_PLAYER, card)
        updateHighlight()
    }

    fun onDrawnCard(): List<Action> {
        val actions = game.drawCard(MAIN_PLAYER)
        updateHighlight()
        return actions
    }

    fun startGame(): List<Action> {
        return game.startGame()
    }

    fun getNextTurnsActions(): List<Action> = mutableListOf<Action>().apply {
        repeat(PLAYERS_COUNT) {
            val receivedActions = game.nextTurn(MAIN_PLAYER)
            addAll(receivedActions)
        }
    }

    private fun updateHighlight() {
        val highlightInfo = cardsHighlighter.provideCardsToHighlight(mainPlayer.hand, game.getTopCard(), cardWasTakenInRound)
        _possibleMoves.value = highlightInfo.first
        _drawPossible.value = highlightInfo.second
    }

    fun onGameStarted() {
        updateHighlight()
    }

    companion object {
        const val MAIN_PLAYER = "Main"
        const val ARTHUR = "Arthur"
        const val JOHN = "John"
        private const val PLAYERS_COUNT = 2
    }
}