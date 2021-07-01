package com.moonlightbutterfly.makao.dataclasses

import androidx.appcompat.widget.AppCompatImageView
import com.moonlightbutterfly.makao.dataclasses.Card

data class CardWrapper(
    val card: Card,
    val imageView: AppCompatImageView,
    var highlighted: Boolean = false
)