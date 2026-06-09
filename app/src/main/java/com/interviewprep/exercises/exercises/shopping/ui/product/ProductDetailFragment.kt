package com.interviewprep.exercises.exercises.shopping.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

class ProductDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val productId = arguments?.getInt("productId") ?: -1
        val product = ShoppingCart.products.firstOrNull { it.id == productId }
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    if (product != null) ProductDetailScreen(
                        emoji = product.emoji,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        onAddToCart = {
                            ShoppingCart.addProduct(product)
                            Toast.makeText(context, "${product.name} added!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    emoji: String, name: String, description: String,
    price: Double, onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(description, color = Color(0xFF9CA3AF), fontSize = 15.sp, modifier = Modifier.padding(top = 8.dp))
        Text("\$$price", color = Color(0xFFF5A623), fontWeight = FontWeight.Bold, fontSize = 28.sp, modifier = Modifier.padding(vertical = 16.dp))
        Button(
            onClick = onAddToCart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5A623))
        ) { Text("Add to Cart", fontSize = 16.sp) }
    }
}
