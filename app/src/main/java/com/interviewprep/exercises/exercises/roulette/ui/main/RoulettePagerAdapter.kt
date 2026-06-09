package com.interviewprep.exercises.exercises.roulette.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.interviewprep.exercises.exercises.roulette.ui.bet.BetFragment
import com.interviewprep.exercises.exercises.roulette.ui.history.HistoryFragment

/**
 * RoulettePagerAdapter
 *
 * Two pages:
 *   Page 0 (left)  → BetFragment   — the betting table
 *   Page 1 (right) → HistoryFragment — the roll history
 *
 * Swipe right from BetFragment to see history.
 * Swipe left from HistoryFragment to go back to betting.
 *
 * Note: FragmentStateAdapter is used instead of the deprecated
 * FragmentPagerAdapter — it properly handles fragment lifecycle
 * and saves/restores state for off-screen pages.
 */
class RoulettePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0    -> BetFragment()
        1    -> HistoryFragment()
        else -> throw IllegalArgumentException("Unknown page position: $position")
    }
}
