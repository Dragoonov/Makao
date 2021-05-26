package com.moonlightbutterfly.makao

import android.widget.ImageView

data class CardWrapper(
    val card: Card,
    val imageView: ImageView,
    var highlighted: Boolean = false
)