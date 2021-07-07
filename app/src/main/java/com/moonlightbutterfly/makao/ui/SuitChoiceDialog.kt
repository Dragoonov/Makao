package com.moonlightbutterfly.makao.ui


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.moonlightbutterfly.makao.R
import com.moonlightbutterfly.makao.Suit
import com.moonlightbutterfly.makao.databinding.SuitChoiceFragmentBinding

class SuitChoiceDialog(private val listener: (suit: Suit) -> Unit) : DialogFragment() {

    private lateinit var binding: SuitChoiceFragmentBinding
    private var suitChosen = Suit.CLUBS

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SuitChoiceFragmentBinding.inflate(layoutInflater).apply {
            this.fragment = this@SuitChoiceDialog
        }

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.suit_choice))
            .setCancelable(false)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                listener(suitChosen)
            }
            .create().apply {
                setCanceledOnTouchOutside(false)
            }
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