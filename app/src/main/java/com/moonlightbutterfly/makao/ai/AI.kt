package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Action

class AI {
    fun getActionsForPlayer(inputBundle: AIInputBundle): AIOutputBundle {
        val (player, stack, sideStack, effects) = inputBundle.clone()
        val actions = mutableListOf<Action>()
        // TODO write logic
        return AIOutputBundle(AIInputBundle(player, stack, sideStack, effects), actions)
    }
}