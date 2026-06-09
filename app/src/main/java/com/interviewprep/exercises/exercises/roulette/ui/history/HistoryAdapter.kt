package com.interviewprep.exercises.exercises.roulette.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.roulette.model.RollResult

/**
 * HistoryAdapter
 *
 * Renders each RollResult row:
 *  - Roll number (0–36)
 *  - Earnings: green for positive, red for negative
 */
class HistoryAdapter : ListAdapter<RollResult, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RollResult>() {
            // RollResult has no unique ID — use position comparison via content only
            override fun areItemsTheSame(old: RollResult, new: RollResult) =
                old.number == new.number && old.earnings == new.earnings
            override fun areContentsTheSame(old: RollResult, new: RollResult) = old == new
        }

        private val WIN_COLOR  = Color.parseColor("#27AE60")
        private val LOSS_COLOR = Color.parseColor("#E74C3C")
        private val TIE_COLOR  = Color.parseColor("#7F8C8D")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_roll_result, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRollNumber: TextView = itemView.findViewById(R.id.tvRollNumber)
        private val tvEarnings: TextView   = itemView.findViewById(R.id.tvEarnings)

        fun bind(result: RollResult) {
            tvRollNumber.text = "Rolled: ${result.number}"
            tvEarnings.text = result.formattedEarnings

            // Color-code earnings: green win, red loss, gray break-even
            val color = when {
                result.earnings > 0 -> WIN_COLOR
                result.earnings < 0 -> LOSS_COLOR
                else                -> TIE_COLOR
            }
            tvEarnings.setTextColor(color)
        }
    }
}
