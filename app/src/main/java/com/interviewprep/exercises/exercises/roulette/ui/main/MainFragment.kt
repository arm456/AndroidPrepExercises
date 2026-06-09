package com.interviewprep.exercises.exercises.roulette.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel

/**
 * MainFragment — outer host for the Roulette ViewPager2.
 *
 * Creates ViewPager2 programmatically (no XML layout needed).
 * The two inner pages (BetFragment, HistoryFragment) now use Compose for their UI,
 * but they're still Fragments — ViewPager2 requires Fragment pages.
 *
 * Interview note: Compose and Fragment/ViewPager2 coexist cleanly.
 * Each Fragment hosts a ComposeView as its root view. The ViewPager2 doesn't
 * know or care whether its fragment pages use Compose or XML internally.
 */
class MainFragment : Fragment() {

    private val viewModel: RouletteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ViewPager2(requireContext()).apply {
        adapter = RoulettePagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        currentItem = 0
    }
}
