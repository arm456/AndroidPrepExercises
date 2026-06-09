package com.interviewprep.exercises.exercises.shopping.ui.checkout

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

/**
 * CheckoutFragment — order confirmation screen.
 *
 * ─── popUpTo and popUpToInclusive (codelab step 6 concept) ───────────────────
 *
 * After the user completes checkout, pressing Back should return them to Home,
 * NOT to Cart (which now has stale data) or the Checkout screen itself.
 *
 * We achieve this with NavOptions:
 *   popUpTo = shop_home_dest     ← pop back to home
 *   popUpToInclusive = false     ← keep home in the back stack (don't pop it too)
 *
 * The action in the nav graph encodes this:
 *   <action
 *       android:id="@+id/action_cart_to_checkout_dest"
 *       app:destination="@+id/shop_checkout_dest"
 *       app:popUpTo="@+id/shop_home_dest"
 *       app:popUpToInclusive="false"/>
 *
 * Result: Checkout → Back → Home (not Cart).
 *
 * ─── Interview note ───────────────────────────────────────────────────────────
 *
 * popUpToInclusive=true would pop Home too, leaving an empty back stack.
 * Use this when you don't want the user to be able to go back at all
 * (e.g. after login flow, or after a one-time splash screen).
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val total = ShoppingCart.totalPrice
        view.findViewById<TextView>(R.id.tvOrderTotal).text =
            "Order Total: $${"%.2f".format(total)}"

        view.findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            ShoppingCart.clear()

            // Navigate back to Home, popping Cart AND Checkout off the stack.
            // Interview note: This matches the nav graph action's popUpTo behavior.
            // After purchase, Back from Home exits the app — correct behavior.
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.shop_home_dest, inclusive = false)
                .build()
            findNavController().navigate(R.id.shop_home_dest, null, navOptions)
        }

        view.findViewById<Button>(R.id.btnBackToCart).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
