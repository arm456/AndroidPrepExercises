package com.interviewprep.exercises.exercises.horizontalscroll.recyclerview

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.exercises.horizontalscroll.HorizontalScrollViewModel

/**
 * HorizontalListFragment — Approach A: RecyclerView
 *
 * Demonstrates a horizontal scrolling list where the user can scroll
 * multiple items at once (no snapping).
 *
 * ─── Center detection strategy ───────────────────────────────────────────────
 *
 * We attach an OnScrollListener. When scrolling stops (SCROLL_STATE_IDLE),
 * we ask the LinearLayoutManager for the first fully visible item position.
 *
 * Why SCROLL_STATE_IDLE and not SCROLL_STATE_DRAGGING?
 * During dragging, the position is mid-flight — not meaningful yet.
 * We want the settled position after momentum decays.
 *
 * Why findFirstCompletelyVisibleItemPosition vs findFirstVisibleItemPosition?
 * "CompletelyVisible" means the item is not clipped at the edge.
 * If you use findFirstVisibleItemPosition, a half-visible card at the left
 * edge counts — leading to off-by-one mismatches with what the user sees.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class HorizontalListFragment : BaseFragment(R.layout.fragment_horizontal_list) {

    // activityViewModels scopes ViewModel to the Activity.
    // This means both fragments (RecyclerView + ViewPager) share the same instance
    // if the Activity is the same — useful for demonstrating the shared pattern.
    private val viewModel: HorizontalScrollViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExerciseCardAdapter
    private lateinit var tvHeader: TextView
    private lateinit var tvFooter: TextView

    override fun onViewReady(savedInstanceState: Bundle?) {
        bindViews()
        setupRecyclerView()
        observeViewModel()
    }

    private fun bindViews() {
        recyclerView = requireView().findViewById(R.id.recyclerViewHorizontal)
        tvHeader = requireView().findViewById(R.id.tvHeader)
        tvFooter = requireView().findViewById(R.id.tvFooter)
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = ExerciseCardAdapter { item, position ->
            // Optional: allow tap to explicitly select an item
            viewModel.onItemCentered(position)
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.submitList(viewModel.items)

        // ── The key scroll listener ────────────────────────────────────────
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Scroll has fully settled — safe to read current position
                    val centerPos = layoutManager.findFirstCompletelyVisibleItemPosition()

                    // Guard: -1 means no item is completely visible (rare edge case
                    // when the list is sized too small or item is larger than viewport)
                    if (centerPos != RecyclerView.NO_ID.toInt()) {
                        viewModel.onItemCentered(centerPos)
                    }
                }
            }
        })
    }

    private fun observeViewModel() {
        // Header and footer observe the same LiveData and update reactively.
        // Neither needs to know HOW the selection happened (scroll vs tap).
        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            tvHeader.text = "📂  ${item.title}"
            tvFooter.text = item.description
        }
    }
}
