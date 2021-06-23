package com.moonlightbutterfly.makao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.makao.effect.*
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter

class GameViewModel : ViewModel() {

    private val john = Player(JOHN, mutableListOf())
    private val arthur = Player(ARTHUR, mutableListOf())
    private val mainPlayer = Player(MAIN_PLAYER, mutableListOf())
    private val game = Game(listOf(john, arthur, mainPlayer))
    private val cardsHighlighter = OptionsHighlighter.instance


    private val _possibleMoves = MutableLiveData<List<Card>>()
    val possibleMoves: LiveData<List<Card>> = _possibleMoves

    private val _gameStarted = MutableLiveData(false)
    val gameStarted: LiveData<Boolean> = _gameStarted

    private val _drawPossible = MutableLiveData<Boolean>()
    val drawPossible: LiveData<Boolean> = _drawPossible

    private val _finishRoundPossible = MutableLiveData<Boolean>()
    val finishRoundPossible: LiveData<Boolean> = _finishRoundPossible

    private val _johnWon = MutableLiveData<Boolean>()
    val johnWon: LiveData<Boolean> = _johnWon

    private val _arthurWon = MutableLiveData<Boolean>()
    val arthurWon: LiveData<Boolean> = _arthurWon

    fun onCardPlacedOnTop(card: CardWrapper, effect: Effect? = null) {
        game.placeCardOnTop(MAIN_PLAYER, card.card, effect)
        updateHighlight()
    }

    fun onDrawnCard(): List<Action> {
        return game.drawCard(MAIN_PLAYER)
    }

    fun startGame(): List<Action> {
        _gameStarted.value = true
        return game.startGame()
    }

    fun getNextTurnsActions(): List<Action> = mutableListOf<Action>().apply {
        repeat(PLAYERS_COUNT) {
            val receivedActions = game.nextTurn(MAIN_PLAYER)
            addAll(receivedActions)
        }
    }

    fun updateHighlight() {
        val highlightInfo = cardsHighlighter.provideOptionsToHighlight(
            mainPlayer.hand,
            game.getTopCard(),
            game.getCardsTakenInRound(),
            game.getCardWasPlacedInRound(),
            game.getCurrentEffect(),
        )
        _possibleMoves.value = highlightInfo.cardsToPlay
        _drawPossible.value = highlightInfo.drawPossible
        _finishRoundPossible.value = highlightInfo.finishRoundPossible
    }

    companion object {
        const val MAIN_PLAYER = "Main"
        const val ARTHUR = "Arthur"
        const val JOHN = "John"
        private const val PLAYERS_COUNT = 2
    }
}