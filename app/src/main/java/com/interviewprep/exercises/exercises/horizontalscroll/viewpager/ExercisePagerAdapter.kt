package com.interviewprep.exercises.exercises.horizontalscroll.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.interviewprep.exercises.core.ExerciseItem

/**
 * ExercisePagerAdapter
 *
 * FragmentStateAdapter for ViewPager2.
 *
 * ─── FragmentStateAdapter vs FragmentPagerAdapter (deprecated) ───────────────
 *
 * FragmentPagerAdapter (ViewPager1) kept all fragments in memory.
 * FragmentStateAdapter (ViewPager2) saves/restores fragment state and destroys
 * off-screen pages beyond the offscreen page limit — much more memory efficient.
 *
 * ─── Why pass items into the adapter rather than loading in the Fragment? ────
 *
 * The adapter is the single source of truth for "what page goes where".
 * Passing items in keeps ExercisePageFragment generic (just renders what it's given).
 * The outer fragment or ViewModel owns the dataset.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class ExercisePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val items: List<ExerciseItem>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        // Each page is its own Fragment instance.
        // ExercisePageFragment receives its data via arguments — the standard
        // Android pattern for passing data into fragments.
        return ExercisePageFragment.newInstance(items[position])
    }
}
