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
import com.moonlightbutterfly.makao.databinding.FragmentGameShownBinding
import com.moonlightbutterfly.makao.databinding.RankChoiceFragmentBinding
import com.moonlightbutterfly.makao.databinding.SuitChoiceFragmentBinding
import com.moonlightbutterfly.makao.effect.RequireRankEffect
import com.moonlightbutterfly.makao.effect.RequireSuitEffect

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