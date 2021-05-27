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
import com.moonlightbutterfly.makao.databinding.FragmentGameShownBinding
import com.moonlightbutterfly.makao.databinding.RankChoiceFragmentBinding
import com.moonlightbutterfly.makao.databinding.SuitChoiceFragmentBinding
import com.moonlightbutterfly.makao.effect.RequireRankEffect
import com.moonlightbutterfly.makao.effect.RequireSuitEffect

class RankChoiceDialog(private val card: CardWrapper) : DialogFragment() {

    private lateinit var binding: RankChoiceFragmentBinding
    private lateinit var viewModel: GameViewModel
    private var rankChosen = Rank.FIVE

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = RankChoiceFragmentBinding.inflate(layoutInflater).apply {
            this.fragment = this@RankChoiceDialog
        }
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.rank_choice))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                (card.effect as RequireRankEffect).setRank(rankChosen)
                viewModel.onCardPlacedOnTop(card)
            }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRank(binding.five, Rank.FIVE)
        super.onViewCreated(view, savedInstanceState)
    }

    fun setRank(view:View, rank: Rank) {
        view as ImageView
        (binding.root as ViewGroup).children.forEach {
            (it as ImageView).setColorFilter(Color.argb(0, 0, 0, 0))
        }
        view.setColorFilter(Color.argb(100, 255, 255, 0))
        rankChosen = rank
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}