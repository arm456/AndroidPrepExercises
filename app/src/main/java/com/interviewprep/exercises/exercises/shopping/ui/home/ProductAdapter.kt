package com.interviewprep.exercises.exercises.shopping.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvEmoji: TextView = view.findViewById(R.id.tvProductEmoji)
        private val tvName: TextView  = view.findViewById(R.id.tvProductName)
        private val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        private val btnView: Button   = view.findViewById(R.id.btnViewProduct)

        fun bind(product: Product) {
            tvEmoji.text = product.emoji
            tvName.text = product.name
            tvPrice.text = "$${product.price}"
            btnView.setOnClickListener { onProductClicked(product) }
            itemView.setOnClickListener { onProductClicked(product) }
        }
    }
}
