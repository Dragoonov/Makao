package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.moonlightbutterfly.makao.databinding.FragmentGameHiddenBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameHiddenBinding
    private var expanded = false
    private val cards: MutableList<Pair<Card, ImageView>> = mutableListOf()
    private val johnCards: MutableList<Pair<Card, ImageView>> = mutableListOf()
    private val arthurCards: MutableList<Pair<Card, ImageView>> = mutableListOf()
    private var cardsDistance: Float = 0f
    private var enemiesCardsDistance: Float = 0f
    private lateinit var imageProvider: CardImageProvider

    private val listener: () -> View.OnTouchListener = {
        object : View.OnTouchListener {
            private var initialX = 0f
            private var initialY = 0f
            private var dX = 0f
            private var dY = 0f

            @SuppressLint("ClickableViewAccessibility")
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
                            if (isCardNearCenter(it)) {
                                it.align(binding.topCard) {
                                    removeCardFromBoard(cards, binding.cardsAnchor, it)
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

    private fun isCardNearCenter(imageView: ImageView): Boolean {
        val top = binding.topCard
        val center = (imageView.x + imageView.width / 2) to (imageView.y + imageView.height / 2)
        val isXInside = center.first > top.x && center.first < top.x + top.width
        val isYInside = center.second > top.y && center.second < top.y + top.height
        return isXInside && isYInside
    }

    private fun arthurDrawCard(card: Card) = enemyDrawCard(card, binding.arthurAnchor, -enemiesCardsDistance, arthurCards)

    private fun johnDrawCard(card: Card) = enemyDrawCard(card, binding.johnAnchor, enemiesCardsDistance, johnCards)

    private fun arthurPlaceCard(card: Card) = enemyPlaceCard(card, binding.arthurAnchor, arthurCards)

    private fun johnPlaceCard(card: Card) = enemyPlaceCard(card, binding.johnAnchor, johnCards)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameHiddenBinding.inflate(layoutInflater)
        binding.layout.setOnClickListener {
            if (expanded) {
                hide().start()
            } else {
                show().start()
            }
        }
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.play.setOnClickListener {
            //drawCard(Card(CardValue.values().random(), CardType.values().random()))
            onRoundFinished()
        }
        binding.stack.setOnClickListener {
            drawCard(Card(CardValue.values().random(), CardType.values().random()))
        }
        binding.topCard.setOnClickListener {
            onRoundFinished()
        }
        imageProvider = CardImageProvider(requireContext())
        cardsDistance = binding.cardsAnchor.layoutParams.width.toFloat()
        enemiesCardsDistance = dpToPixel(ENEMIES_DISTANCE_DP, requireContext())
        return binding.root
    }

    private fun onRoundFinished() {
        val actions = viewModel.getNextTurnsActions()
        val animations = getAnimationsForActions(actions).toMutableList()
        animations.add(0, hide())
        animations.add(show())
        AnimatorSet().apply {
            playSequentially(animations)
            start()
        }
    }

    private fun getAnimationsForActions(actions: List<Action>): List<Animator> {
        val animations = mutableListOf<Animator>()
        actions.forEach {
            val animation = when (it) {
                is DrawCard -> if (it.player.name == GameViewModel.ARTHUR) {
                    arthurDrawCard(it.card)
                } else {
                    johnDrawCard(it.card)
                }
                is PlaceCard -> if (it.player.name == GameViewModel.ARTHUR) {
                    arthurPlaceCard(it.card)
                } else {
                    johnPlaceCard(it.card)
                }
                else -> null
            }
            animation?.let { anim -> animations.add(anim) }
        }
        return animations
    }

    private fun hide(): Animator {
        updateConstraints(R.layout.fragment_game_hidden)
        val cardsAnimations = mutableListOf<Animator>()
        cardsAnimations.addAll(cards.map { it.second.hide() })
        cardsAnimations.addAll((johnCards + arthurCards).map { it.second.enemyShow() })
        expanded = !expanded
        return AnimatorSet().apply {
            playTogether(cardsAnimations)
            duration = 1100
        }
    }

    private fun show(): Animator {
        updateConstraints(R.layout.fragment_game_shown)
        val cardsAnimations = mutableListOf<Animator>()
        cardsAnimations.addAll(cards.map { it.second.show() })
        cardsAnimations.addAll((johnCards + arthurCards).map { it.second.enemyHide() })
        expanded = !expanded
        return AnimatorSet().apply {
            playTogether(cardsAnimations)
            duration = 1100
        }
    }

    private fun reorderHand(
        initialPosition: Float,
        distance: Float,
        cards: List<Pair<Card, ImageView>>,
        duration: Long = 250
    ) {
        cards.map { it.second }.forEachIndexed { index, imageView ->
            imageView.moveX(initialPosition + index * distance, duration)
        }
    }

    private fun recalculateCardsDistance() {
        val spaceTakenRatio = cardsDistance * cards.size / binding.layout.width
        cardsDistance = when {
            spaceTakenRatio > 0.8 -> {
                cardsDistance / 1.5f
            }
            spaceTakenRatio < 0.5 && cardsDistance < binding.cardsAnchor.width -> {
                cardsDistance * 1.5f
            }
            else -> {
                cardsDistance
            }
        }
    }

    private fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(context, id)
        val transition = ChangeBounds().apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 1000
        }
        newConstraintSet.applyTo(binding.layout)
        TransitionManager.beginDelayedTransition(binding.layout, transition)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawCard(card: Card) {
        arthurDrawCard(card).start()
        johnDrawCard(card).start()
        val newView = getCardView(card)
        binding.layout.addView(newView)
        cards.add(card to newView)
        newView.setOnTouchListener(listener())
        newView.animateDrawing(
            binding.stack,
            binding.cardsAnchor,
            binding.cardsAnchor.width + cardsDistance * (cards.size - 1)
        ) {
            recalculateCardsDistance()
            reorderHand(binding.cardsAnchor.width.toFloat(), cardsDistance, cards)
        }
    }

    private fun enemyDrawCard(
        card: Card,
        anchor: ImageView,
        distance: Float,
        cards: MutableList<Pair<Card, ImageView>>
    ): Animator {
        val newView = getCardView(card)
        binding.layout.addView(newView)
        cards.add(card to newView)
        return newView.enemyDrawCardAnimation(
            binding.stack,
            anchor,
            anchor.width + distance * cards.size
        )
    }

    private fun enemyPlaceCard(
        card: Card,
        anchor: ImageView,
        cards: MutableList<Pair<Card, ImageView>>
    ): Animator {
        val pair = cards.find { it.first == card }
        return pair?.second?.placeCardOnTopAnimation(
            targetDrawable = imageProvider.provideCardImageRotated(pair.first)!!) {
            removeCardFromBoard(cards, anchor, pair.second)
        }!!
    }

    private fun getCardView(card:Card): ImageView {
        return ImageView(context).apply {
            setImageDrawable(imageProvider.provideCardImage(card))
            z = 10.0f
            layoutParams = ConstraintLayout.LayoutParams(binding.cardsAnchor.layoutParams)
        }
    }

    private fun removeCardFromBoard(cards: MutableList<Pair<Card, ImageView>>, anchor: ImageView, card: ImageView) {
        var distance = enemiesCardsDistance
        var anchorSource = anchor.x
        if (anchor == binding.cardsAnchor) {
            recalculateCardsDistance()
            distance = cardsDistance
            anchorSource = anchor.width.toFloat()
        }
        val movedPair = cards.first { pair -> pair.second == card }
        cards.removeIf { pair -> pair.first == movedPair.first }
        binding.topCard.setImageDrawable(
            imageProvider.provideCardImage(movedPair.first)
        )
        binding.layout.removeView(movedPair.second)
        reorderHand(
            anchorSource,
            distance,
            cards
        )
    }

    private companion object {
        const val ENEMIES_DISTANCE_DP = 10f
    }

}