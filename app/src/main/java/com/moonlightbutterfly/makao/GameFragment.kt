package com.moonlightbutterfly.makao

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.moonlightbutterfly.makao.databinding.FragmentGameHiddenBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameHiddenBinding
    private var expanded = false
    private val cards: MutableList<Pair<Card, ImageView>> = mutableListOf()
    private var cardsDistance: Float = 0f
    private lateinit var imageProvider: CardImageProvider

    private val listener: () -> View.OnTouchListener = {
        object : View.OnTouchListener {
            private var initialX = 0f
            private var initialY = 0f
            private var dX = 0f
            private var dY = 0f

            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return when (event?.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        v?.let {
                            initialX = v.x
                            initialY = v.y
                            dX = v.x - event.rawX
                            dY = v.y - event.rawY
                        }
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        v?.let {
                            it.y = event.rawY + dY
                            it.x = event.rawX + dX
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        v?.let {
                            it as ImageView
                            val top = binding.topCard
                            val center = (it.x + it.width / 2) to (it.y + it.height / 2)
                            val isXInside = center.first > top.x && center.first < top.x + top.width
                            val isYInside =
                                center.second > top.y && center.second < top.y + top.height
                            if (isXInside && isYInside) {
                                it.moveToCard(binding.topCard) {
                                    val movedCard = cards.first { pair -> pair.second == it }.first
                                    cards.removeIf { pair -> pair.first == movedCard }
                                    binding.topCard.setImageDrawable(
                                        imageProvider.provideCardImage(movedCard)
                                    )
                                    binding.layout.removeView(it)
                                    cardsDistance = calculateCardsDistance(
                                        binding.layout.width, cardsDistance, cards.size, binding.cardsAnchor.width)
                                    shuffleCards()
                                }
                            } else {
                                it.moveBack(initialX, initialY)
                            }
                        }
                        true
                    }
                    else -> return true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameHiddenBinding.inflate(layoutInflater)
        binding.layout.setOnClickListener {
            if (expanded) {
                hide()
            } else {
                show()
            }
        }
        val viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.play.setOnClickListener {
            drawCard(Card(CardValue.values().random(), CardType.values().random()))
        }
        imageProvider = CardImageProvider(requireContext())
        cardsDistance = binding.cardsAnchor.layoutParams.width.toFloat()
        return binding.root
    }

    private fun hide() {
        updateConstraints(R.layout.fragment_game_hidden)
        cards.map { it.second }.forEach { it.hide() }
        expanded = !expanded
    }

    private fun show() {
        updateConstraints(R.layout.fragment_game_shown)
        cards.map { it.second }.forEach { it.show() }
        expanded = !expanded
    }

    private fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(context, id)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 1000
        newConstraintSet.applyTo(binding.layout)
        TransitionManager.beginDelayedTransition(binding.layout, transition)
    }

    private fun shuffleCards() {
        val initialX = binding.cardsAnchor.width
        cards.map { it.second }.forEachIndexed { index, imageView ->
            imageView.moveX(initialX + index * cardsDistance)
        }
    }

    private fun calculateCardsDistance(
        generalWidth: Int,
        currentDistance: Float,
        cardsAmount: Int,
        maximum: Int
    ): Float {
        val spaceTakenRatio = currentDistance * cardsAmount / generalWidth
        return when {
            spaceTakenRatio > 0.8 -> { currentDistance / 1.5f }
            spaceTakenRatio < 0.5 && currentDistance < maximum -> { currentDistance * 1.5f }
            else -> { currentDistance }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawCard(card: Card) {
        val newView = ImageView(context).apply {
            setImageDrawable(imageProvider.provideCardImage(card))
            z = 10.0f
            layoutParams = ConstraintLayout.LayoutParams(binding.cardsAnchor.layoutParams)
        }
        binding.layout.addView(newView)
        cards.add(card to newView)
        newView.setOnTouchListener(listener())
        newView.animateDrawing(binding.stack, binding.cardsAnchor, binding.cardsAnchor.width + cardsDistance * (cards.size-1)) {
            cardsDistance = calculateCardsDistance(
                binding.layout.width, cardsDistance, cards.size, binding.cardsAnchor.width)
            shuffleCards()
        }
    }
}