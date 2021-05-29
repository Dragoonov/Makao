package com.moonlightbutterfly.makao


import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.makao.databinding.SuitChoiceFragmentBinding
import com.moonlightbutterfly.makao.effect.RequireSuitEffect

class SuitChoiceDialog(private val card: CardWrapper) : DialogFragment() {

    private lateinit var binding: SuitChoiceFragmentBinding
    private lateinit var viewModel: GameViewModel
    private var suitChosen = Suit.HEARTS

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SuitChoiceFragmentBinding.inflate(layoutInflater).apply {
            this.fragment = this@SuitChoiceDialog
        }
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.suit_choice))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.onCardPlacedOnTop(card, RequireSuitEffect(suitChosen))
            }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSuit(binding.clubs, Suit.CLUBS)
        super.onViewCreated(view, savedInstanceState)
    }

    fun setSuit(view: View, suit: Suit) {
        view as ImageView
        (binding.root as ViewGroup).children.forEach {
            (it as ImageView).setColorFilter(Color.argb(0, 0, 0, 0))
        }
        view.setColorFilter(Color.argb(100, 255, 255, 0))
        suitChosen = suit

    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}