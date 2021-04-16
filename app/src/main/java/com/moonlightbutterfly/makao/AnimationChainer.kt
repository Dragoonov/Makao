package com.moonlightbutterfly.makao

import android.animation.Animator
import androidx.core.animation.doOnEnd

class AnimationChainer {

    private lateinit var animations: List<() -> Animator>
    private var nextAnim: Animator? = null
    private var index = 0

    fun start() {
        index = 0
        provideNextAnim()
    }

    fun provideAnimations(list: List<() -> Animator>) {
        animations = list
    }

    private fun provideNextAnim() {
        if (index < animations.size) {
            nextAnim = animations[index++]()
            nextAnim?.apply {
                doOnEnd { provideNextAnim() }
                start()
            }
        }
    }
}