package com.moonlightbutterfly.makao.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.R
import com.moonlightbutterfly.makao.databinding.FragmentGameShownBinding
import com.moonlightbutterfly.makao.effect.RequireRankEffect
import com.moonlightbutterfly.makao.effect.RequireSuitEffect

class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentGameShownBinding
    private val cards: MutableList<CardWrapper> = mutableListOf()
    private val johnCards: MutableList<CardWrapper> = mutableListOf()
    private val arthurCards: MutableList<CardWrapper> = mutableListOf()
    private var cardsDistance: Float = 0f
    private var enemiesCardsDistance: Float = 0f
    private lateinit var imageProvider: CardImageProvider
    private val animationChainer = AnimationChainer()

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
                            val cardWrapper = cards.first { wrapper -> wrapper.imageView == it }
                            if (Utils.isCardNearCenter(it, binding.topCard) && cardWrapper.highlighted) {
                                when {
                                    cardWrapper.card.isAce() -> SuitChoiceDialog { suit ->
                                        gameViewModel.onCardPlacedOnTop(cardWrapper, RequireSuitEffect(suit))
                                    }.show(
                                        childFragmentManager,
                                        SuitChoiceDialog.TAG
                                    )
                                    cardWrapper.card.isJack() -> RankChoiceDialog { rank ->
                                        gameViewModel.onCardPlacedOnTop(cardWrapper, RequireRankEffect(rank))
                                    }.show(
                                        childFragmentManager,
                                        RankChoiceDialog.TAG
                                    )
                                    else -> gameViewModel.onCardPlacedOnTop(cardWrapper)
                                }
                                it.align(binding.topCard) {
                                    removeCardFromBoard(cards, binding.cardsAnchor, it, true)
                                }.start()
                            } else {
                                it.moveBack(initialX, initialY).start()
                            }
                        }
                        true
                    }
                    else -> true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java).apply{
            effectLiveData.observe(viewLifecycleOwner) {
                binding.effect.text = when (it) {
                    is RequireSuitEffect -> {
                        getString(R.string.effect_needed, it.getSuit())
                    }
                    is RequireRankEffect -> {
                        getString(R.string.effect_needed, it.getRank())
                    }
                    else -> {
                        ""
                    }
                }
            }
            animationsToPerform.observe(viewLifecycleOwner) {
                val animations = mutableListOf<() -> Animator>().apply {
                    addAll(getAnimationsForActions(it))
                }
                lockActions(true)
                animationChainer.start(animations) {
                    lockActions(false)
                    gameViewModel.updateHighlight()
                }
            }
            gameEnded.observe(viewLifecycleOwner) {
                binding.end.visibility = View.VISIBLE
                binding.end.text = getString(R.string.game_ended, it)
            }
            drawPossible.observe(viewLifecycleOwner) {
                binding.deck.apply {
                    lock(it.not())
                    Utils.highlightCard(this, it)
                }
            }
            finishRoundPossible.observe(viewLifecycleOwner) {
                binding.finishRound.animate().apply {
                    interpolator = BounceInterpolator()
                    duration = 750
                    scaleX(if (it) 1f else 0f)
                    scaleY(if (it) 1f else 0f)
                    start()
                }
                binding.finishRound.apply {
                    lock(it.not())
                    Utils.highlightCard(this, it)
                }
            }
            possibleMoves.observe(viewLifecycleOwner) { list ->
                cards.forEach { Utils.highlightCard(it, list.contains(it.card)) }
            }
        }
        binding = FragmentGameShownBinding.inflate(layoutInflater).apply {
            panelContainer.visibility = View.VISIBLE
            start.setOnClickListener {
                panelContainer.visibility = View.GONE
                gameViewModel.onStartGame()
            }
            deck.setOnClickListener {
                lockActions(true)
                gameViewModel.onDrawnCard()
            }
            finishRound.setOnClickListener { onRoundFinished() }
        }
        imageProvider = CardImageProvider(requireContext())
        cardsDistance = binding.cardsAnchor.layoutParams.width.toFloat()
        enemiesCardsDistance = Utils.dpToPixel(ENEMIES_DISTANCE_DP, requireContext())
        return binding.root
    }

    private fun arthurDrawCard(card: Card) = enemyDrawCard(card, binding.arthurAnchor, -enemiesCardsDistance, arthurCards)

    private fun johnDrawCard(card: Card) = enemyDrawCard(card, binding.johnAnchor, enemiesCardsDistance, johnCards)

    private fun enemyDrawCard(card: Card, anchor: ImageView, distance: Float, cards: MutableList<CardWrapper>): Animator {
        val newView = getCardView(card, true)
        binding.layout.addView(newView)
        cards.add(CardWrapper(card, newView))
        return newView.drawAnimation(
            binding.deck,
            anchor,
            distance * (cards.size - 1)
        )
    }

    private fun arthurPlaceCard(card: Card) = enemyPlaceCard(card, binding.arthurAnchor, arthurCards)

    private fun johnPlaceCard(card: Card) = enemyPlaceCard(card, binding.johnAnchor, johnCards)

    private fun enemyPlaceCard(
        card: Card,
        anchor: ImageView,
        cards: MutableList<CardWrapper>
    ): Animator {
        val wrapper = cards.first { it.card == card }
        return wrapper.imageView.placeCardOnTopAnimation(
            card = binding.topCard,
            targetDrawable = imageProvider.provideCardImageRotated(wrapper.card)
        ) {
            removeCardFromBoard(cards, anchor, wrapper.imageView)
        }
    }

    private fun onRoundFinished() {
        lockActions(true)
        gameViewModel.onTurnFinished()
    }

    private fun lockActions(lock: Boolean) {
        binding.root.lock(lock)
    }

    private fun getAnimationsForActions(actions: List<Action>): List<() -> Animator> {
        val animations = mutableListOf<() -> Animator>()
        actions.forEach {
            val animation = when (it) {
                is HideInterfaceAction -> { { hide() } }
                is ShowInterfaceAction -> { { show() } }
                is InitializeCardAction -> {
                    { initializeCard(it.card) }
                }
                is DrawCardAction -> when (it.player.name) {
                    GameViewModel.ARTHUR -> {
                        { arthurDrawCard(it.card) }
                    }
                    GameViewModel.JOHN -> {
                        { johnDrawCard(it.card) }
                    }
                    else -> {
                        { drawCard(it.card) }
                    }
                }
                is PlaceCardAction -> if (it.player.name == GameViewModel.ARTHUR) {
                    { arthurPlaceCard(it.card) }
                } else {
                    { johnPlaceCard(it.card) }
                }
                else -> error("Wrong action")
            }
            animation.let { anim -> animations.add(anim) }
        }
        return animations
    }

    private fun hide(): Animator {
        val cardsAnimations = mutableListOf<Animator>()
        cardsAnimations.addAll(cards.map { it.imageView.hide() })
        cardsAnimations.addAll((johnCards + arthurCards).map { it.imageView.enemyShow() })
        return AnimatorSet().apply {
            playTogether(cardsAnimations)
            doOnStart { updateConstraints(R.layout.fragment_game_hidden) }
        }
    }

    private fun show(): Animator {
        val cardsAnimations = mutableListOf<Animator>()
        cardsAnimations.addAll(cards.map { it.imageView.show() })
        cardsAnimations.addAll((johnCards + arthurCards).map { it.imageView.enemyHide() })
        return AnimatorSet().apply {
            playTogether(cardsAnimations)
            doOnStart { updateConstraints(R.layout.fragment_game_shown) }
        }
    }

    private fun reorderHand(
        initialPosition: Float,
        distance: Float,
        cards: List<CardWrapper>,
        duration: Long = 250
    ) {
        cards.map { it.imageView }.forEachIndexed { index, imageView ->
            imageView.moveX(initialPosition + index * distance, duration)
        }
    }

    private fun recalculateCardsDistance() {
        val spaceTakenRatio = cardsDistance * cards.size / binding.layout.width
        cardsDistance = when {
            spaceTakenRatio > 0.8 -> cardsDistance / 1.5f
            spaceTakenRatio < 0.5 && cardsDistance < binding.cardsAnchor.width -> cardsDistance * 1.5f
            else -> cardsDistance
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
    private fun drawCard(card: Card): Animator {
        val newView = getCardView(card)
        binding.layout.addView(newView)
        cards.add(CardWrapper(card, newView))
        newView.setOnTouchListener(listener())
        return newView.drawAnimation(
            binding.deck,
            binding.cardsAnchor,
            cardsDistance * (cards.size - 1)
        ) {
            recalculateCardsDistance()
            reorderHand(binding.cardsAnchor.x, cardsDistance, cards)
        }
    }

    private fun initializeCard(card: Card): Animator {
        val cardView = getCardView(card, params = binding.deck.layoutParams)
        binding.layout.addView(cardView)
        return cardView.initializeTopCard(
            binding.deck,
            binding.topCard
        ) {
            binding.topCard.setImageDrawable(
                imageProvider.provideCardImage(card)
            )
            binding.layout.removeView(cardView)
        }
    }

    private fun getCardView(
        card: Card,
        forEnemy: Boolean = false,
        params: ViewGroup.LayoutParams = binding.cardsAnchor.layoutParams
    ): ImageView {
        return ImageView(context).apply {
            val drawable = if (forEnemy) imageProvider.provideCardBackImage() else imageProvider.provideCardImage(card)
            setImageDrawable(drawable)
            z = 10.0f
            layoutParams = ConstraintLayout.LayoutParams(params)
        }
    }

    private fun removeCardFromBoard(
        cards: MutableList<CardWrapper>,
        anchor: ImageView,
        card: ImageView,
        recalculateDistance: Boolean = false
    ) {
        val movedCardWrapper = cards.first { wrapper -> wrapper.imageView == card }
        cards.removeIf { wrapper -> wrapper.card == movedCardWrapper.card }
        binding.topCard.setImageDrawable(
            imageProvider.provideCardImage(movedCardWrapper.card)
        )
        binding.layout.removeView(movedCardWrapper.imageView)
        var distance = if (anchor == binding.johnAnchor) enemiesCardsDistance else -enemiesCardsDistance
        if (recalculateDistance) {
            recalculateCardsDistance()
            distance = cardsDistance
        }
        reorderHand(anchor.x, distance, cards)
    }

    private fun ImageView.enemyHide() = this.move(70f) { a, b -> a - b }
    private fun ImageView.enemyShow() = this.move(70f) { a, b -> a + b }
    private fun ImageView.hide() = this.move(382f) { a, b -> a + b }
    private fun ImageView.show() = this.move(382f) { a, b -> a - b }
    private fun Card.isAce() = this.rank == Rank.ACE
    private fun Card.isJack() = this.rank == Rank.JACK

    companion object {
        private const val ENEMIES_DISTANCE_DP = 10f
    }
}