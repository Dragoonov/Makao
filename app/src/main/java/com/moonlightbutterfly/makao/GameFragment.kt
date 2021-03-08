package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.moonlightbutterfly.makao.databinding.FragmentGameHiddenBinding

class GameFragment: Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameHiddenBinding
    private var expanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameHiddenBinding.inflate(layoutInflater)
        binding.playerDeck.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CardAdapter().apply {
                addCard((Card(CardValue.ACE, CardType.SPADES)))
            }
        }
        binding.layout.setOnClickListener {
            show()
        }
        val viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.play.setOnClickListener {
            transferDrawnCard()
        }
        return binding.root
    }

    private fun show() {
        if (expanded) {
            updateConstraints(R.layout.fragment_game_hidden)
        } else {
            updateConstraints(R.layout.fragment_game_shown)
        }
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

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun transferDrawnCard() {
        val newView = ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.queen_diamonds))
            z = 100.0f
            layoutParams = ConstraintLayout.LayoutParams(binding.stack.layoutParams)
        }
        binding.layout.addView(newView)
        val propertyX = PropertyValuesHolder.ofFloat("x", binding.stack.x, binding.playerDeck.x + binding.playerDeck.width)
        val propertyY = PropertyValuesHolder.ofFloat("y", binding.stack.y, binding.playerDeck.y)
        val propertyShrinkX = PropertyValuesHolder.ofFloat(
            "scaleX",
            1f,
            binding.playerDeck.height / binding.stack.height.toFloat()
        )
        val propertyShrinkY = PropertyValuesHolder.ofFloat(
            "scaleY",
            1f,
            binding.playerDeck.height / binding.stack.height.toFloat()
        )
        val shrinkAnim = ObjectAnimator.ofPropertyValuesHolder(
            newView,
            propertyShrinkX,
            propertyShrinkY
        ).apply {
            duration = 1000
        }
        val disappearAnim = ObjectAnimator.ofFloat(newView, View.ALPHA, 0f).apply {
            duration = 1000
        }
        val moveAnim = ObjectAnimator.ofPropertyValuesHolder(newView, propertyX, propertyY).apply {
            duration = 1000
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    drawCard()
                    binding.layout.removeView(newView)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    // Nothing
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    // Nothing
                }

            })
        }
        AnimatorSet().apply {
            playTogether(moveAnim, shrinkAnim, disappearAnim)
            start()
        }

    }
    private fun drawCard() {
        (binding.playerDeck.adapter as CardAdapter).addCard(Card(CardValue.JACK, CardType.DIAMONDS))
    }
}