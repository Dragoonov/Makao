package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.widget.ImageView

fun ImageView.show() = this.move(dpToPixel(-275f, context))

fun ImageView.hide() = this.move(dpToPixel(275f, context))

fun ImageView.moveBack(initialX: Float, initialY: Float) {
    val x = PropertyValuesHolder.ofFloat("x", this.x, initialX)
    val y = PropertyValuesHolder.ofFloat("y", this.y, initialY)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, x, y).apply {
        duration = 250
    }
    AnimatorSet().apply {
        play(moveAnim)
        start()
    }
}

fun ImageView.moveX(targetX: Float) {
    val x = PropertyValuesHolder.ofFloat("x", this.x, targetX)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, x).apply {
        duration = 250
    }
    AnimatorSet().apply {
        play(moveAnim)
        start()
    }
}

fun ImageView.moveToCard(card: ImageView, atEnd: () -> Unit) {
    val x = PropertyValuesHolder.ofFloat("x", this.x, card.x + card.width/2 - this.width/2)
    val y = PropertyValuesHolder.ofFloat("y", this.y, card.y + card.height/2 - this.height/2)
    val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, card.width.toFloat()/this.width)
    val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, card.height.toFloat()/this.height)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, x, y).apply {
        duration = 250
    }
    val shrinkAnim = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        duration = 250
    }
    AnimatorSet().apply {
        playTogether(moveAnim, shrinkAnim)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) { atEnd() }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

        })
        start()
    }
}

fun ImageView.highlight(highlight: Boolean) {
    if (highlight) {
        setColorFilter(Color.argb(100, 255, 255, 0))
    } else {
        setColorFilter(Color.argb(0, 0, 0, 0))
    }
}

private fun ImageView.move(dp: Float) {
    val translationY = PropertyValuesHolder.ofFloat("translationY", this.translationY, this.translationY + dp)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        duration = 1000
    }
    AnimatorSet().apply {
        play(moveAnim)
        start()
    }
}

fun ImageView.animateDrawing(source: ImageView, target: ImageView, distance: Float, atEnd: () -> Unit = {}) {
    val propertyX = PropertyValuesHolder.ofFloat("x", source.x, distance)
    val propertyY = PropertyValuesHolder.ofFloat("y", source.y, target.y)
    val propertyShrinkX = PropertyValuesHolder.ofFloat(
        "scaleX",
        source.width.toFloat() / target.width,
        1f,
    )
    val propertyShrinkY = PropertyValuesHolder.ofFloat(
        "scaleY",
        source.height.toFloat() / target.height,
        1f
    )
    val shrinkAnim = ObjectAnimator.ofPropertyValuesHolder(
        this,
        propertyShrinkX,
        propertyShrinkY
    ).apply {
        duration = 1000
    }
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, propertyX, propertyY).apply {
        duration = 1000
    }
    AnimatorSet().apply {
        playTogether(moveAnim, shrinkAnim)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) { atEnd() }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        start()
    }
}
fun dpToPixel(dp: Float, context: Context) = dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
