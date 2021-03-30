package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Action

data class AIOutputBundle (
    val bundle: AIInputBundle,
    val actions: List<Action>
)