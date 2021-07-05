package com.moonlightbutterfly.makao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter

@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(
    private val highlighter: OptionsHighlighter,
    private val gameProvider: (List<String>) -> Game) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(cardsHighlighter = highlighter, gameProvider = gameProvider) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}