package com.interviewprep.exercises.exercises.shopping.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.Product
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

/**
 * HomeFragment — the product listing screen.
 *
 * ─── Navigation patterns demonstrated ────────────────────────────────────────
 *
 * 1. Navigate by ACTION ID with argument (Safe Args pattern):
 *    findNavController().navigate(HomeFragmentDirections.actionHomeToProductDest(id))
 *
 * 2. Navigate by DESTINATION ID (direct, no args):
 *    findNavController().navigate(R.id.shop_cart_dest)
 *
 * 3. Overflow menu navigation via NavigationUI.onNavDestinationSelected():
 *    Menu item ID matches destination ID → NavigationUI navigates automatically.
 *
 * ─── Milestone 2: Extra cart entry point ─────────────────────────────────────
 * The "View Cart" button at the bottom is the third path to CartFragment,
 * alongside bottom nav and drawer.
 */
class HomeFragment : Fragment(R.layout.fragment_shop_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupProductList(view)
        setupCartButton(view)
        updateCartBadge(view)
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_overflow_home, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // NavigationUI.onNavDestinationSelected returns true if the
                // menu item ID matched a nav graph destination and was handled.
                // Fall back to false — super.onOptionsItemSelected() is an Activity
                // method, not callable from inside a Fragment's MenuProvider lambda.
                return androidx.navigation.ui.NavigationUI
                    .onNavDestinationSelected(menuItem, findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupProductList(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProductAdapter(ShoppingCart.products) { product ->
            navigateToProduct(product)
        }
    }

    private fun navigateToProduct(product: Product) {
        val action = HomeFragmentDirections.actionHomeToProductDest(product.id)
        findNavController().navigate(action)
    }

    private fun setupCartButton(view: View) {
        // ── Milestone 2: third cart entry point ──────────────────────────
        // Plain Button (not FAB) — type must match what's in the XML layout.
        // FloatingActionButton extends ImageButton, not Button, so casting
        // to Button causes a ClassCastException at runtime.
        view.findViewById<Button>(R.id.btnGoToCart).setOnClickListener {
            findNavController().navigate(R.id.shop_cart_dest)
        }
    }

    private fun updateCartBadge(view: View) {
        view.findViewById<TextView>(R.id.tvCartBadge).text =
            ShoppingCart.totalItems.toString()
    }

    override fun onResume() {
        super.onResume()
        view?.let { updateCartBadge(it) }
    }
}
