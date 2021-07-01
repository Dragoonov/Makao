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
import com.moonlightbutterfly.makao.Rank
import com.moonlightbutterfly.makao.databinding.RankChoiceFragmentBinding

class RankChoiceDialog(private val listener: (rank: Rank) -> Unit) : DialogFragment() {

    private lateinit var binding: RankChoiceFragmentBinding
    private var rankChosen = Rank.FIVE

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = RankChoiceFragmentBinding.inflate(layoutInflater).apply {
            this.fragment = this@RankChoiceDialog
        }
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.rank_choice))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                listener(rankChosen)
            }
            .create()
    }

    fun setRank(view:View, rank: Rank) {
        view as AppCompatImageView
        (binding.root as ViewGroup).children.forEach {
            (it as AppCompatImageView).setColorFilter(requireContext().getColor(R.color.no_tint))
        }
        view.setColorFilter(requireContext().getColor(R.color.highlight))
        rankChosen = rank
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}