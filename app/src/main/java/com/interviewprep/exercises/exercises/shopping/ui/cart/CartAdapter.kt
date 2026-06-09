package com.interviewprep.exercises.exercises.shopping.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.CartItem

class CartAdapter(
    private val items: List<CartItem>,
    private val onRemove: (productId: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView     = view.findViewById(R.id.tvCartItemName)
        private val tvQty: TextView      = view.findViewById(R.id.tvCartItemQty)
        private val tvSubtotal: TextView = view.findViewById(R.id.tvCartItemSubtotal)
        private val btnRemove: Button    = view.findViewById(R.id.btnRemoveCartItem)

        fun bind(item: CartItem) {
            tvName.text = "${item.product.emoji} ${item.product.name}"
            tvQty.text = "Qty: ${item.quantity}"
            tvSubtotal.text = "$${"%.2f".format(item.subtotal)}"
            btnRemove.setOnClickListener { onRemove(item.product.id) }
        }
    }
}
