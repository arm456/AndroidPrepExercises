package com.interviewprep.exercises.exercises.roulette.ui.history

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel

/**
 * HistoryFragment — Milestone 1 & 2
 *
 * Displays the roll history list. Accessible by swiping right from BetFragment.
 *
 * ─── Milestone 2: Auto-refresh without pull-to-refresh ───────────────────────
 *
 * This fragment observes rollResults LiveData from the shared RouletteViewModel.
 * When BetFragment calls viewModel.roll(), the ViewModel updates _rollResults
 * with a new ImmutableList reference → LiveData notifies this observer →
 * adapter.submitList() re-diffs and animates the new row in automatically.
 *
 * No polling. No manual refresh. No callbacks between fragments.
 * The ViewModel is the single source of truth; both fragments are just views.
 *
 * ─── Why this doesn't observe bets ───────────────────────────────────────────
 *
 * HistoryFragment only subscribes to _rollResults — not _bets or _totalMoney.
 * This means incrementing a bet in BetFragment triggers zero work in this
 * fragment. See RouletteViewModel for the full Q3 architectural rationale.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    private val viewModel: RouletteViewModel by activityViewModels()
    private lateinit var adapter: HistoryAdapter
    private lateinit var tvEmptyState: TextView

    override fun onViewReady(savedInstanceState: Bundle?) {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerViewHistory)
        tvEmptyState = requireView().findViewById(R.id.tvEmptyHistory)

        adapter = HistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // ── Observe only rollResults — bet changes never trigger this ────────
        viewModel.rollResults.observe(viewLifecycleOwner) { results ->
            val list = results.toList().reversed()  // newest at top
            adapter.submitList(list)
            tvEmptyState.visibility = if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
