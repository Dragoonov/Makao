package com.moonlightbutterfly.makao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val john = Player(JOHN, mutableListOf())
    private val arthur = Player(ARTHUR, mutableListOf())

    private val game = Game(listOf(john, arthur))
    private val _johnDrawCard = MutableLiveData<Card>()

    val johnDrawCard: LiveData<Card> = _johnDrawCard
    private val _arthurDrawCard = MutableLiveData<Card>()

    val arthurDrawCard: LiveData<Card> = _arthurDrawCard
    private val _johnPlaceCard = MutableLiveData<Card>()

    val johnPlaceCard: LiveData<Card> = _johnPlaceCard
    private val _arthurPlaceCard = MutableLiveData<Card>()

    val arthurPlaceCard: LiveData<Card> = _arthurPlaceCard
    private val _switchPlayerTurn = MutableLiveData<Boolean>()

    val switchPlayerTurn: LiveData<Boolean> = _switchPlayerTurn
    private val _possibleMoves = MutableLiveData<List<Card>>()

    val possibleMoves: LiveData<List<Card>> = _possibleMoves
    private val _drawPossible = MutableLiveData<Boolean>()

    val drawPossible: LiveData<Boolean> = _drawPossible
    private val _johnWon = MutableLiveData<Boolean>()

    val johnWon: LiveData<Boolean> = _johnWon
    private val _arthurWon = MutableLiveData<Boolean>()

    val arthurWon: LiveData<Boolean> = _arthurWon

    fun onCardPlacedOnTop(card: Card) {

    }

    fun onDrawnCard(card: Card) {

    }

    fun onRoundFinished() {
        _switchPlayerTurn.value = true
    }

    fun getNextTurnsActions(): List<Action> = mutableListOf<Action>().apply {
        repeat(PLAYERS_COUNT) {
            val receivedActions = game.nextTurn()
            addAll(receivedActions)
        }
    }

    companion object {
        const val ARTHUR = "Arthur"
        const val JOHN = "John"
        private const val PLAYERS_COUNT = 2
    }
}