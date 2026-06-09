package com.interviewprep.exercises.exercises.shopping.model

/**
 * Shopping app data model.
 *
 * Kept intentionally simple — this exercise focuses on Navigation, not data layer.
 */

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val emoji: String
)

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Double get() = product.price * quantity
}

/**
 * ShoppingCart — simple in-memory singleton for this exercise.
 *
 * Interview note: In a real app this would be a Repository + ViewModel.
 * Here it's a singleton so all fragments share state without ViewModel plumbing
 * that would distract from the Navigation concepts being demonstrated.
 */
object ShoppingCart {

    val products: List<Product> = listOf(
        Product(1, "Artisan Coffee",   "Single-origin, medium roast",    12.99, "☕"),
        Product(2, "Wireless Earbuds", "48hr battery, noise cancelling", 79.99, "🎧"),
        Product(3, "Notebook",         "Hardcover, 200 pages, dotted",    9.99, "📓"),
        Product(4, "Succulent Plant",  "Low maintenance, comes potted",  14.99, "🌵"),
        Product(5, "Mechanical Keyboard", "TKL, brown switches",         99.99, "⌨️"),
        Product(6, "Desk Lamp",        "Adjustable, USB-C charging",     34.99, "💡"),
    )

    private val _items = mutableMapOf<Int, CartItem>()

    val items: List<CartItem> get() = _items.values.toList()

    val totalItems: Int get() = _items.values.sumOf { it.quantity }

    val totalPrice: Double get() = _items.values.sumOf { it.subtotal }

    fun addProduct(product: Product) {
        val existing = _items[product.id]
        _items[product.id] = if (existing != null) {
            existing.copy(quantity = existing.quantity + 1)
        } else {
            CartItem(product, 1)
        }
    }

    fun removeItem(productId: Int) {
        _items.remove(productId)
    }

    fun clear() {
        _items.clear()
    }

    fun isEmpty() = _items.isEmpty()
}
