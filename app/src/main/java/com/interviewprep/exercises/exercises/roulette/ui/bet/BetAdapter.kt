package com.interviewprep.exercises.exercises.roulette.ui.bet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.roulette.model.BetOption

/**
 * BetAdapter — renders the bet options list.
 *
 * bettingEnabled starts true. Only set to false by Milestone 3's over-budget
 * guard (totalBet >= totalMoney). The ROLL button is controlled separately.
 *
 * Bug fix: removed the `if (bettingEnabled == enabled) return` early-exit guard.
 * That guard blocked the very first call from BetFragment.bindViews() where
 * the adapter needed to explicitly confirm the initial enabled=true state.
 */
class BetAdapter(
    private val onIncrementClicked: (betId: Int) -> Unit
) : ListAdapter<BetOption, BetAdapter.BetViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BetOption>() {
            override fun areItemsTheSame(old: BetOption, new: BetOption) = old.id == new.id
            override fun areContentsTheSame(old: BetOption, new: BetOption) = old == new
        }
    }

    // Default true — +$1 buttons are enabled until the user goes over-budget
    private var bettingEnabled = true

    fun setBettingEnabled(enabled: Boolean) {
        bettingEnabled = enabled
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bet_option, parent, false)
        return BetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BetViewHolder, position: Int) {
        holder.bind(getItem(position), bettingEnabled)
    }

    inner class BetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLabel: TextView    = itemView.findViewById(R.id.tvBetLabel)
        private val tvAmount: TextView   = itemView.findViewById(R.id.tvBetAmount)
        private val btnIncrement: Button = itemView.findViewById(R.id.btnIncrementBet)

        fun bind(bet: BetOption, canBet: Boolean) {
            tvLabel.text  = bet.label
            tvAmount.text = if (bet.amount > 0) "\$${bet.amount}" else "\$0"

            btnIncrement.isEnabled = canBet
            btnIncrement.alpha = if (canBet) 1.0f else 0.4f
            btnIncrement.setOnClickListener {
                onIncrementClicked(bet.id)
            }
        }
    }
}
