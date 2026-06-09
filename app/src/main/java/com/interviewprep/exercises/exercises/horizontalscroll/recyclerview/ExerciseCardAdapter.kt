package com.interviewprep.exercises.exercises.horizontalscroll.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.core.ExerciseItem
import com.interviewprep.exercises.R

/**
 * ExerciseCardAdapter
 *
 * RecyclerView adapter for the horizontal list exercise.
 *
 * ─── Why ListAdapter + DiffUtil? ────────────────────────────────────────────
 *
 * ListAdapter (a subclass of RecyclerView.Adapter) accepts a DiffUtil.ItemCallback.
 * When you call submitList(), it diffs old vs new list on a background thread and
 * posts only the changed positions — no more notifyDataSetChanged() blowing away
 * the whole list and losing scroll position.
 *
 * Interview tip: Always mention DiffUtil when asked about RecyclerView performance.
 * "I use ListAdapter so updates are diffed on a background thread. notifyDataSetChanged
 * is a last resort — it's O(n) and kills animations."
 *
 * ────────────────────────────────────────────────────────────────────────────
 */
class ExerciseCardAdapter(
    private val onItemClicked: (ExerciseItem, Int) -> Unit
) : ListAdapter<ExerciseItem, ExerciseCardAdapter.CardViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExerciseItem>() {
            // Called first — cheap structural check
            override fun areItemsTheSame(old: ExerciseItem, new: ExerciseItem) =
                old.id == new.id

            // Called only when areItemsTheSame returns true — deep content check
            override fun areContentsTheSame(old: ExerciseItem, new: ExerciseItem) =
                old == new  // data class == checks all fields
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvCardSubtitle)
        private val colorBar: View = itemView.findViewById(R.id.viewColorBar)

        fun bind(item: ExerciseItem, position: Int) {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            colorBar.setBackgroundColor(Color.parseColor(item.colorHex))

            itemView.setOnClickListener {
                onItemClicked(item, position)
            }
        }
    }
}
