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
    private val cardsHighlighter = OptionsHighlighter.instance
    private val game = Game(listOf(john, arthur, mainPlayer)).apply {
        setOnEffectListener {
            _effectLiveData.postValue(it)
        }
    }

    private val _animationsToPerform = MutableLiveData<List<Action>>()
    val animationsToPerform: LiveData<List<Action>> = _animationsToPerform

    private val _possibleMoves = MutableLiveData<List<Card>>()
    val possibleMoves: LiveData<List<Card>> = _possibleMoves

    private val _effectLiveData = MutableLiveData<Effect>()
    val effectLiveData: LiveData<Effect> = _effectLiveData

    private val _drawPossible = MutableLiveData<Boolean>()
    val drawPossible: LiveData<Boolean> = _drawPossible

    private val _finishRoundPossible = MutableLiveData<Boolean>()
    val finishRoundPossible: LiveData<Boolean> = _finishRoundPossible

    private val _gameEnded = MutableLiveData<String>()
    val gameEnded: LiveData<String> = _gameEnded

    fun onCardPlacedOnTop(card: CardWrapper, effect: Effect? = null) {
        game.placeCardOnTop(MAIN_PLAYER, card.card, effect)
        if (mainPlayer.hand.isEmpty()) {
            _gameEnded.postValue(MAIN_PLAYER)
        }
        updateHighlight()
    }

    fun onDrawnCard() {
        _animationsToPerform.postValue(game.drawCard(MAIN_PLAYER))
    }

    fun onStartGame() {
        _animationsToPerform.postValue(game.startGame())
    }

    fun onTurnFinished() {
        game.onTurnEnd()
        _animationsToPerform.postValue(mutableListOf<Action>().apply {
            add(HideInterfaceAction())
            addAll(getNextTurnsActions())
            add(ShowInterfaceAction())
        })
    }

    private fun getNextTurnsActions(): List<Action> = mutableListOf<Action>().apply {
        repeat(PLAYERS_COUNT) {
            val receivedActions = game.nextTurn(MAIN_PLAYER)
            addAll(receivedActions)
            if (arthur.hand.isEmpty()) {
                _gameEnded.postValue(ARTHUR)
            } else if (john.hand.isEmpty()) {
                _gameEnded.postValue(JOHN)
            }
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
        const val MAIN_PLAYER = "You"
        const val ARTHUR = "Arthur"
        const val JOHN = "John"
        private const val PLAYERS_COUNT = 2
    }
}