package com.interviewprep.exercises.exercises.shopping.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

/**
 * ProductDetailFragment
 *
 * ─── Safe Args: reading arguments ────────────────────────────────────────────
 *
 * The productId argument was passed via HomeFragmentDirections.actionHomeToProductDest(id).
 * We read it from the fragment's arguments bundle.
 *
 * With the Safe Args plugin, you'd use the generated Args class:
 *   val args by navArgs<ProductDetailFragmentArgs>()
 *   val productId = args.productId
 *
 * Without the plugin (our approach here), we read directly from arguments:
 *   val productId = arguments?.getInt("productId")
 *
 * The runtime behavior is identical — Safe Args just adds compile-time safety.
 *
 * ─── Transition animations ────────────────────────────────────────────────────
 *
 * The action in the nav graph specifies enter/exit animations:
 *   app:enterAnim="@anim/slide_in_right"
 *   app:exitAnim="@anim/slide_out_left"
 *   app:popEnterAnim="@anim/slide_in_left"
 *   app:popExitAnim="@anim/slide_out_right"
 *
 * These are defined in res/anim/ and applied automatically by NavController.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Read argument — Safe Args equivalent of: val args by navArgs<...>()
        val productId = arguments?.getInt("productId") ?: return
        val product = ShoppingCart.products.firstOrNull { it.id == productId } ?: return

        view.findViewById<TextView>(R.id.tvDetailEmoji).text = product.emoji
        view.findViewById<TextView>(R.id.tvDetailName).text = product.name
        view.findViewById<TextView>(R.id.tvDetailDescription).text = product.description
        view.findViewById<TextView>(R.id.tvDetailPrice).text = "$${product.price}"

        view.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            ShoppingCart.addProduct(product)
            Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
        }
    }
}
