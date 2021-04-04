package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Action
import com.moonlightbutterfly.makao.BoardState
import com.moonlightbutterfly.makao.Player
import com.moonlightbutterfly.makao.clone

class AI {
    fun getActionsForPlayer(player: Player, boardState: BoardState): Triple<BoardState, List<Action>, Player> {
        val clonedPlayer = player.copy(name = player.name, hand = player.hand)
        val clonedBoardState = boardState.clone()
        val actions = mutableListOf<Action>()
        // TODO write logic
        return Triple(clonedBoardState, actions, player)
    }
}