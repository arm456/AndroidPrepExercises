package com.interviewprep.exercises.exercises.shopping.ui.cart

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

/**
 * CartFragment
 *
 * ─── Navigation concepts demonstrated ────────────────────────────────────────
 *
 * 1. This is a TOP-LEVEL destination (listed in AppBarConfiguration's
 *    topLevelDestinationIds), so it shows a hamburger icon, not a back arrow.
 *    The user treats it as a peer of Home, not a child.
 *
 * 2. This is also the target of the DEEP LINK defined in the nav graph:
 *    <deepLink app:uri="www.shoppingapp.example.com/cart"/>
 *    Deep links allow external apps / notifications to navigate directly here.
 *    Interview note: NavController handles deep link resolution automatically —
 *    you just define the URI in the nav graph, no code needed in the Fragment.
 *
 * 3. Checkout uses popUpTo + popUpToInclusive to clear the cart from the
 *    back stack after purchase, so Back doesn't return to a stale cart.
 *
 * ─── Milestone 2: This destination is reachable 3 ways ───────────────────────
 *
 *   a) Bottom navigation cart icon  → NavigationUI.setupWithNavController
 *   b) Navigation drawer "Cart"     → NavigationUI.setupWithNavController
 *   c) Home screen floating button  → findNavController().navigate(R.id.shop_cart_dest)
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class CartFragment : Fragment(R.layout.fragment_cart) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshCart(view)
    }

    override fun onResume() {
        super.onResume()
        view?.let { refreshCart(it) }
    }

    private fun refreshCart(view: View) {
        val tvEmpty = view.findViewById<TextView>(R.id.tvCartEmpty)
        val rvCart = view.findViewById<RecyclerView>(R.id.rvCart)
        val tvTotal = view.findViewById<TextView>(R.id.tvCartTotal)
        val btnCheckout = view.findViewById<Button>(R.id.btnCheckout)

        if (ShoppingCart.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
            tvTotal.visibility = View.GONE
            btnCheckout.isEnabled = false
            btnCheckout.alpha = 0.4f
        } else {
            tvEmpty.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
            tvTotal.visibility = View.VISIBLE
            btnCheckout.isEnabled = true
            btnCheckout.alpha = 1.0f

            rvCart.layoutManager = LinearLayoutManager(requireContext())
            rvCart.adapter = CartAdapter(ShoppingCart.items) { productId ->
                ShoppingCart.removeItem(productId)
                refreshCart(view)
            }

            tvTotal.text = "Total: $${"%.2f".format(ShoppingCart.totalPrice)}"
        }

        btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_checkout_dest)
        }
    }
}
