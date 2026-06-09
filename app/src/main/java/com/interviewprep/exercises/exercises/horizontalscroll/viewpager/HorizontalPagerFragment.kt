package com.interviewprep.exercises.exercises.horizontalscroll.viewpager

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.exercises.horizontalscroll.HorizontalScrollViewModel

/**
 * HorizontalPagerFragment — Approach B: ViewPager2
 *
 * The "outer" host fragment. Contains the ViewPager2 and the header/footer
 * UI that updates when the selected page changes.
 *
 * ─── How snapping works ──────────────────────────────────────────────────────
 *
 * ViewPager2 enforces one-item-at-a-time snapping by default.
 * Unlike RecyclerView, no SnapHelper needed — it's built in.
 * The user can fling but always lands on a complete page.
 *
 * ─── How center detection works ──────────────────────────────────────────────
 *
 * Two options:
 *   1. viewPager.currentItem — synchronous, returns the current page index
 *   2. OnPageChangeCallback.onPageSelected — fires reactively on each page settle
 *
 * We use option 2 here because it naturally maps to ViewModel updates:
 *   page changes → ViewModel.onItemCentered() → LiveData → header/footer update
 *
 * ─── Fragment architecture note ──────────────────────────────────────────────
 *
 * This is the "outer" fragment. ExercisePageFragment is the "inner" page.
 * Header and footer live HERE — they're not inside page fragments.
 * This is intentional: the header/footer show context about the selected page,
 * so they belong to the container, not the page itself.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class HorizontalPagerFragment : BaseFragment(R.layout.fragment_horizontal_pager) {

    private val viewModel: HorizontalScrollViewModel by activityViewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var tvHeader: TextView
    private lateinit var tvFooter: TextView
    private lateinit var tvPageIndicator: TextView

    override fun onViewReady(savedInstanceState: Bundle?) {
        bindViews()
        setupViewPager()
        observeViewModel()
    }

    private fun bindViews() {
        viewPager = requireView().findViewById(R.id.viewPagerHorizontal)
        tvHeader = requireView().findViewById(R.id.tvHeader)
        tvFooter = requireView().findViewById(R.id.tvFooter)
        tvPageIndicator = requireView().findViewById(R.id.tvPageIndicator)
    }

    private fun setupViewPager() {
        val pagerAdapter = ExercisePagerAdapter(
            fragmentManager = childFragmentManager,  // childFragmentManager for nested fragments
            lifecycle = viewLifecycleOwner.lifecycle,
            items = viewModel.items
        )

        viewPager.adapter = pagerAdapter

        // ── The key page change listener ───────────────────────────────────
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            /**
             * Called when a new page is selected (after snapping completes).
             * Equivalent to RecyclerView's SCROLL_STATE_IDLE + findFirstCompletelyVisible.
             *
             * This is the primary update hook for ViewPager2.
             */
            override fun onPageSelected(position: Int) {
                viewModel.onItemCentered(position)
                updatePageIndicator(position)
            }
        })
    }

    private fun observeViewModel() {
        // Exactly the same observation pattern as the RecyclerView approach.
        // The ViewModel is the bridge — neither fragment knows about the other.
        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            tvHeader.text = "📂  ${item.title}"
            tvFooter.text = item.description
        }
    }

    private fun updatePageIndicator(position: Int) {
        val total = viewModel.items.size
        tvPageIndicator.text = "${position + 1} / $total"
    }
}
