package com.interviewprep.exercises

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

/**
 * MainActivity
 *
 * Single Activity — Navigation Component handles all screen transitions.
 *
 * ─── Why single Activity? ────────────────────────────────────────────────────
 * Fragments share the same Activity ViewModel scope, making cross-screen
 * state sharing (like the shared HorizontalScrollViewModel) trivial.
 * The Navigation Component handles backstack, arguments, and transitions.
 * ─────────────────────────────────────────────────────────────────────────────
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
