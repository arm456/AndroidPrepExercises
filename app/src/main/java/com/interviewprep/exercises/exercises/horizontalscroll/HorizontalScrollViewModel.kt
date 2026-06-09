package com.interviewprep.exercises.exercises.horizontalscroll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.interviewprep.exercises.core.ExerciseItem
import com.interviewprep.exercises.core.ExerciseRegistry

/**
 * HorizontalScrollViewModel
 *
 * Shared ViewModel for BOTH the RecyclerView and ViewPager2 implementations.
 * Scoped to the Activity (or NavGraph) so both approaches can share it.
 *
 * ─── Why a shared ViewModel instead of callbacks? ───────────────────────────
 *
 * In the ViewPager2 approach, page fragments (inner) need to communicate
 * with the outer container fragment to update the header/footer.
 *
 * Bad pattern — interface callback:
 *   inner fragment → interface → outer fragment
 *   Problem: tight coupling, interface leaks if not cleared, breaks on rotation.
 *
 * Good pattern — shared ViewModel:
 *   inner fragment → viewModel.onItemCentered() → outer fragment observes LiveData
 *   Benefits: survives rotation, lifecycle-safe, zero coupling between fragments.
 *
 * ────────────────────────────────────────────────────────────────────────────
 */
class HorizontalScrollViewModel : ViewModel() {

    // All items (in a real app, this comes from a Repository)
    val items: List<ExerciseItem> = ExerciseRegistry.sampleExerciseItems

    // The currently "centered" or selected item
    private val _selectedItem = MutableLiveData<ExerciseItem>().apply {
        value = items.firstOrNull()
    }
    val selectedItem: LiveData<ExerciseItem> = _selectedItem

    // The current position, tracked separately so observers don't need to indexOf()
    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition: LiveData<Int> = _selectedPosition

    /**
     * Called by:
     *  - RecyclerView scroll listener when scroll state becomes IDLE
     *  - ViewPager2.OnPageChangeCallback.onPageSelected
     *
     * Both approaches funnel into the same ViewModel method — the UI layer
     * doesn't need to know which scroll mechanism is in use.
     */
    fun onItemCentered(position: Int) {
        if (position < 0 || position >= items.size) return
        if (_selectedPosition.value == position) return  // no-op if same

        _selectedPosition.value = position
        _selectedItem.value = items[position]
    }
}
