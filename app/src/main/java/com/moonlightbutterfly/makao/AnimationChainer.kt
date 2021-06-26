package com.moonlightbutterfly.makao

import android.animation.Animator
import androidx.core.animation.doOnEnd

class AnimationChainer {

    private lateinit var animations: List<() -> Animator>
    private var nextAnim: Animator? = null
    private var index = 0
    private var doOnEnd: () -> Unit = {}

    fun start(list: List<() -> Animator>, doOnEnd: () -> Unit = {}) {
        animations = list
        index = 0
        this.doOnEnd = doOnEnd
        provideNextAnim()
    }

    fun stop() {
        nextAnim?.pause()
    }

    private fun provideNextAnim() {
        if (index < animations.size) {
            nextAnim = animations[index++]()
            nextAnim?.apply {
                doOnEnd { provideNextAnim() }
                start()
            }
        } else {
            doOnEnd()
        }
    }
}