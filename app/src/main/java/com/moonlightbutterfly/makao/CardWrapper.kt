package com.moonlightbutterfly.makao

import androidx.appcompat.widget.AppCompatImageView

data class CardWrapper(
    val card: Card,
    val imageView: AppCompatImageView,
    var highlighted: Boolean = false
)