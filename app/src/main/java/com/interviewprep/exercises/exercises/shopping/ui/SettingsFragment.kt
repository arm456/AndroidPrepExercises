package com.interviewprep.exercises.exercises.shopping.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.interviewprep.exercises.R

/**
 * SettingsFragment
 *
 * Reached via the overflow menu (⋮) on the home screen.
 *
 * ─── NavigationUI.onNavDestinationSelected ────────────────────────────────────
 *
 * In HomeFragment, the options menu item for Settings has:
 *   android:id="@+id/shop_settings_dest"
 *
 * That ID matches this fragment's destination ID in the nav graph.
 * NavigationUI.onNavDestinationSelected() detects the match and calls
 * findNavController().navigate(R.id.shop_settings_dest) automatically.
 *
 * This is the codelab's "menu navigation" pattern (step 7):
 * You don't write any navigation code — just name the menu item ID to match
 * the destination ID, and NavigationUI handles the rest.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class SettingsFragment : Fragment(R.layout.fragment_shop_settings) {
    // No navigation code needed here — NavigationUI handles arriving at this
    // destination. The back button returns to wherever we came from automatically.
}
