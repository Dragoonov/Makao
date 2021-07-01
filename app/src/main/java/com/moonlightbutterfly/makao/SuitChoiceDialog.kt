package com.moonlightbutterfly.makao


import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.makao.databinding.SuitChoiceFragmentBinding
import com.moonlightbutterfly.makao.effect.RequireSuitEffect

class SuitChoiceDialog(private val listener: (suit: Suit) -> Unit) : DialogFragment() {

    private lateinit var binding: SuitChoiceFragmentBinding
    private var suitChosen = Suit.HEARTS

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SuitChoiceFragmentBinding.inflate(layoutInflater).apply {
            this.fragment = this@SuitChoiceDialog
        }

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.suit_choice))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                listener(suitChosen)
            }
            .create()
    }

    fun setSuit(view: View, suit: Suit) {
        view as AppCompatImageView
        (binding.root as ViewGroup).children.forEach {
            (it as AppCompatImageView).setColorFilter(requireContext().getColor(R.color.no_tint))
        }
        view.setColorFilter(requireContext().getColor(R.color.highlight))
        suitChosen = suit

    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}