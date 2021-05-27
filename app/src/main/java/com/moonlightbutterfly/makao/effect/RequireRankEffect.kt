package com.moonlightbutterfly.makao.effect

import com.moonlightbutterfly.makao.Rank

class RequireRankEffect (private var rank: Rank): Effect {
    fun getRank() = rank
    fun setRank(rank: Rank) {
        this.rank = rank
    }

    override fun merge(effect: Effect): Effect = effect
}