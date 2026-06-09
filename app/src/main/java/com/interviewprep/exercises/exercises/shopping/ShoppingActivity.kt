package com.interviewprep.exercises.exercises.shopping

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.interviewprep.exercises.R

/**
 * ShoppingActivity — Exercise 03: Jetpack Navigation Codelab
 *
 * This is the single Activity that hosts the entire shopping cart navigation graph.
 * It demonstrates:
 *
 * ─── 1. NavHostFragment ────────────────────────────────────────────────────────
 * The activity layout contains a NavHostFragment that swaps fragment destinations
 * in and out. The activity itself owns the toolbar, bottom nav, and nav drawer —
 * all "global" UI that persists across destinations.
 *
 * ─── 2. AppBarConfiguration ────────────────────────────────────────────────────
 * Controls which destinations show a hamburger menu (≡) vs a back arrow (←).
 * Top-level destinations (home, cart) show the hamburger; all others show back.
 *
 * ─── 3. NavigationUI ───────────────────────────────────────────────────────────
 * Wires up bottom nav and drawer nav to the NavController automatically.
 * When a bottom nav item is selected, NavigationUI calls navigate() for you.
 * The label in the nav graph XML becomes the ActionBar title automatically.
 *
 * ─── 4. Milestone 2: Extra Cart Entry Point ────────────────────────────────────
 * The shopping cart is reachable from THREE places:
 *   a) Bottom navigation bar cart icon
 *   b) Navigation drawer "Cart" item
 *   c) NEW: floating cart button on the HomeFragment (Milestone 2 addition)
 * This is the "extra navigation entry point" the designer requested.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class ShoppingActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.shopping_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val drawerLayout = findViewById<DrawerLayout>(R.id.shopping_drawer_layout)
        val navView = findViewById<NavigationView>(R.id.shopping_nav_view)
        val bottomNav = findViewById<BottomNavigationView>(R.id.shopping_bottom_nav)

        // Top-level destinations show hamburger, not back arrow.
        // Interview note: Any destination listed here is treated as a "root" —
        // the Up button becomes a drawer toggle instead of a back arrow.
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.shop_home_dest,
                R.id.shop_cart_dest
            ),
            drawerLayout = drawerLayout
        )

        setSupportActionBar(findViewById(R.id.shopping_toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Wire up the drawer — NavigationUI handles item selection automatically
        navView.setupWithNavController(navController)

        // Wire up bottom nav — NavigationUI handles item selection automatically
        // Interview note: NavigationUI.setupWithNavController ensures that
        // navigating to a bottom nav destination pops the back stack back to
        // the start destination, matching the standard Android UX convention.
        NavigationUI.setupWithNavController(bottomNav, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        // NavigationUI.navigateUp handles both drawer toggle and NavController.navigateUp()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
