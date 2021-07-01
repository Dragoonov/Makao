package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Action
import com.moonlightbutterfly.makao.BoardState
import com.moonlightbutterfly.makao.Player

data class AIOutput(val boardState: BoardState, val actions: List<Action>, val player: Player)