package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.widget.ImageView

private fun ImageView.animation(
    property: String = "",
    sourceX: Float = this.x,
    sourceY: Float = this.y,
    targetX: Float = this.x,
    targetY: Float = this.y,
    durationAnim: Long = 1000,
    atEnd: () -> Unit = {}
): ObjectAnimator {
    val x = PropertyValuesHolder.ofFloat(
        if (property.isEmpty()) "x" else property + "X",
        sourceX,
        targetX
    )
    val y = PropertyValuesHolder.ofFloat(
        if (property.isEmpty()) "y" else property + "Y",
        sourceY,
        targetY
    )
    return ObjectAnimator.ofPropertyValuesHolder(this, x, y).apply {
        duration = durationAnim
        object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        }
    }
}

fun ImageView.moveBack(initialX: Float, initialY: Float): Animator {
    val animation = this.animation(targetX = initialX, targetY = initialY, durationAnim = 250)
    return AnimatorSet().apply {
        play(animation)
    }
}

fun ImageView.moveX(targetX: Float, durationAnim: Long) {
    val animation = this.animation(targetX = targetX, durationAnim = durationAnim)
    AnimatorSet().apply {
        play(animation)
        start()
    }
}

fun ImageView.placeCardOnTopAnimation(card: ImageView, durationAnim: Long = 1000, targetDrawable: Drawable, atEnd: () -> Unit = {}): Animator {
    val animator = this.align(card, durationAnim) as AnimatorSet
    val flipX = PropertyValuesHolder.ofFloat("rotationX", 0f, 90f)
    val flipX2 = PropertyValuesHolder.ofFloat("rotationX", 90f, 180f)
    val flip2Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX2).apply {
        duration = 750
    }
    val flip1Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX).apply {
        duration = 250

        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                this@placeCardOnTopAnimation.setImageDrawable(targetDrawable)
                this@placeCardOnTopAnimation.rotationY = 180f
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
    return animator.apply {
        play(flip2Anim).after(flip1Anim)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

        })
    }
}

fun ImageView.align(card: ImageView, durationAnim: Long = 250, atEnd: () -> Unit = {}): Animator {
    val moveAnimation = animation(
        sourceX = this.x,
        targetX = card.x + card.width / 2 - this.width / 2,
        sourceY = this.y,
        targetY = card.y + card.height / 2 - this.height / 2,
        durationAnim = durationAnim
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = this.scaleX,
        targetX = card.width.toFloat() / this.width,
        sourceY = this.scaleY,
        targetY = card.height.toFloat() / this.height,
        durationAnim = durationAnim
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

        })
    }
}

fun ImageView.highlight(highlight: Boolean) {
    if (highlight) {
        setColorFilter(Color.argb(100, 255, 255, 0))
    } else {
        setColorFilter(Color.argb(0, 0, 0, 0))
    }
}

fun ImageView.translateY(source: Float, target: Float): Animator {
    val translationY =
        PropertyValuesHolder.ofFloat("translationY", source, target)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        duration = 1000
    }
    return AnimatorSet().apply {
        play(moveAnim)
    }
}

fun ImageView.drawAnimation(
    source: ImageView,
    target: ImageView,
    distance: Float,
    atEnd: () -> Unit = {}
): Animator {
    val moveAnimation = animation(
        sourceX = source.x,
        targetX = target.x + distance,
        sourceY = source.y,
        targetY = target.y
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = source.width.toFloat() / target.width,
        targetX = 1f,
        sourceY = source.height.toFloat() / target.height,
        targetY = 1f
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
}

fun dpToPixel(dp: Float, context: Context) =
    dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
