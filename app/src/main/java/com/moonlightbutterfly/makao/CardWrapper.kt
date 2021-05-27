package com.moonlightbutterfly.makao

import android.widget.ImageView
import com.moonlightbutterfly.makao.effect.Effect

data class CardWrapper(
    val card: Card,
    val imageView: ImageView,
    var highlighted: Boolean = false,
    val effect: Effect? = null
)