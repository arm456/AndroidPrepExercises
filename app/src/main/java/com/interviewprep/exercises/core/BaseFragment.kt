package com.interviewprep.exercises.core

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * BaseFragment
 *
 * Thin base class for all exercise fragments.
 * Keep it minimal — only add shared behavior that every fragment genuinely needs.
 *
 * Current responsibilities:
 *  - Enforce a layout res at construction time (avoids boilerplate onCreateView)
 *  - Provide a hook [onViewReady] that fires after the view is created and restored
 *
 * Interview note: Why not put all setup in onViewCreated?
 * onViewCreated is fine for most cases. A base class hook is useful when you want
 * to enforce an ordering guarantee (e.g., always restore state before binding data).
 */
abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(savedInstanceState)
    }

    /**
     * Called after view creation and state restoration.
     * Override this instead of onViewCreated in subclasses.
     */
    protected open fun onViewReady(savedInstanceState: Bundle?) {}
}
