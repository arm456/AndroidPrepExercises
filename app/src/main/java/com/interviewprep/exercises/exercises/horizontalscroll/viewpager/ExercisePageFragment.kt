package com.interviewprep.exercises.exercises.horizontalscroll.viewpager

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.core.ExerciseItem
import com.interviewprep.exercises.exercises.horizontalscroll.HorizontalScrollViewModel

/**
 * ExercisePageFragment — one page inside the ViewPager2
 *
 * This is the "inner" fragment. It receives one ExerciseItem via arguments
 * and renders it. It does NOT own any state — state lives in the ViewModel.
 *
 * ─── Communication pattern ────────────────────────────────────────────────────
 *
 * This fragment could theoretically update things in the "outer" fragment
 * (HorizontalPagerFragment). The clean way to do this is through the shared
 * ViewModel — write to it here, observe it there.
 *
 * We do NOT use:
 *   - Interface callbacks (tight coupling, rotation issues)
 *   - parentFragment casts (fragile, null-risk)
 *   - EventBus (global state, hard to trace)
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class ExercisePageFragment : BaseFragment(R.layout.fragment_exercise_page) {

    companion object {
        private const val ARG_ITEM_ID = "arg_item_id"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_SUBTITLE = "arg_subtitle"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_COLOR = "arg_color"

        /**
         * Factory method — preferred over public constructor with args.
         * Keeps argument keys private to this class.
         */
        fun newInstance(item: ExerciseItem): ExercisePageFragment {
            return ExercisePageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_ID, item.id)
                    putString(ARG_TITLE, item.title)
                    putString(ARG_SUBTITLE, item.subtitle)
                    putString(ARG_DESCRIPTION, item.description)
                    putString(ARG_COLOR, item.colorHex)
                }
            }
        }
    }

    // Shared with HorizontalPagerFragment — same Activity scope
    private val viewModel: HorizontalScrollViewModel by activityViewModels()

    override fun onViewReady(savedInstanceState: Bundle?) {
        val args = requireArguments()
        val title = args.getString(ARG_TITLE, "")
        val subtitle = args.getString(ARG_SUBTITLE, "")
        val description = args.getString(ARG_DESCRIPTION, "")
        val color = args.getString(ARG_COLOR, "#4A90E2")

        requireView().apply {
            findViewById<TextView>(R.id.tvPageTitle).text = title
            findViewById<TextView>(R.id.tvPageSubtitle).text = subtitle
            findViewById<TextView>(R.id.tvPageDescription).text = description
            findViewById<View>(R.id.viewPageColorStrip).setBackgroundColor(Color.parseColor(color))
        }
    }
}
