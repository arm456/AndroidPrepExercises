package com.interviewprep.exercises.exercises.shopping.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.fragment.findNavController
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.Product
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                ShopHomeScreen(
                    onProductClick = { product ->
                        val action = HomeFragmentDirections.actionHomeToProductDest(product.id)
                        findNavController().navigate(action)
                    },
                    onCartClick = {
                        findNavController().navigate(R.id.shop_cart_dest)
                    }
                )
            }
        }
    }

    // Refresh cart count when returning from product screen
    override fun onResume() {
        super.onResume()
        (view as? ComposeView)?.invalidate()
    }
}

@Composable
fun ShopHomeScreen(onProductClick: (Product) -> Unit, onCartClick: () -> Unit) {
    val cartCount = remember { mutableStateOf(ShoppingCart.totalItems) }

    // Recompose cart badge whenever screen becomes active
    LaunchedEffect(Unit) { cartCount.value = ShoppingCart.totalItems }

    Column(Modifier.fillMaxSize().background(Color(0xFF1A1A2E))) {
        LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
            items(ShoppingCart.products) { product ->
                ProductCard(product = product, onClick = { onProductClick(product) })
            }
        }

        // ── Milestone 2: Extra cart entry point ──────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16213E))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Items in cart: ${cartCount.value}", color = Color(0xFF9CA3AF), fontSize = 13.sp)
            Button(
                onClick = { cartCount.value = ShoppingCart.totalItems; onCartClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5A623))
            ) { Text("View Cart →", fontSize = 13.sp) }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(product.emoji, fontSize = 32.sp, modifier = Modifier.padding(end = 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("\$${product.price}", color = Color(0xFFF5A623), fontSize = 13.sp)
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) { Text("View", fontSize = 12.sp) }
        }
    }
}
