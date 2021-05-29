package com.moonlightbutterfly.makao.effect

import com.moonlightbutterfly.makao.Rank

class RequireRankEffect (private val rank: Rank): Effect {
    fun getRank() = rank
    override fun merge(effect: Effect?): Effect = effect ?: this
}