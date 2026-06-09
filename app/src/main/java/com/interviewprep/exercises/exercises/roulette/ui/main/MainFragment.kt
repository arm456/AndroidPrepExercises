package com.interviewprep.exercises.exercises.roulette.ui.main

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel
import com.interviewprep.exercises.exercises.roulette.ui.bet.BetFragment
import com.interviewprep.exercises.exercises.roulette.ui.history.HistoryFragment

/**
 * MainFragment — the outer host fragment for the Roulette exercise.
 *
 * ─── Milestone 4 Q1: How many fragments and why? ─────────────────────────────
 *
 * We added 3 fragments:
 *
 *   1. MainFragment (this file) — hosts the ViewPager2.
 *      ViewPager2 requires one Fragment per "page", so we need an outer
 *      container to hold the pager itself. This fragment owns no visible UI
 *      beyond the ViewPager — it's purely structural.
 *
 *   2. BetFragment — the left page. Shows betting controls.
 *
 *   3. HistoryFragment — the right page. Shows roll history.
 *      Accessible by swiping right. Swiping left returns to BetFragment.
 *
 * Could there be fewer?
 *   Yes — if we restructured completely, BetFragment and HistoryFragment
 *   could be merged into a single fragment using a horizontal split layout
 *   (e.g., SlidingPaneLayout). Then MainFragment would also be unnecessary.
 *   Minimum: 1 fragment (or even 0 fragments with a pure Activity approach).
 *
 * In practice, the separate fragment approach is preferred for:
 *   - Clear separation of concerns
 *   - Independent lifecycle management
 *   - Easier to extend (add more swipe pages later)
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class MainFragment : BaseFragment(R.layout.fragment_roulette_main) {

    // ViewModel is scoped to the activity — same instance across all 3 fragments
    private val viewModel: RouletteViewModel by activityViewModels()

    override fun onViewReady(savedInstanceState: Bundle?) {
        val viewPager = requireView().findViewById<ViewPager2>(R.id.viewPagerRoulette)

        val adapter = RoulettePagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = viewLifecycleOwner.lifecycle
        )
        viewPager.adapter = adapter

        // Start on the BetFragment (page 0)
        viewPager.currentItem = 0
    }
}
