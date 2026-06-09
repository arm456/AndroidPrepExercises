package com.interviewprep.exercises.exercises.shopping.ui.cart

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
import com.interviewprep.exercises.exercises.shopping.model.CartItem
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

class CartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                CartScreen(onCheckout = {
                    findNavController().navigate(R.id.action_cart_to_checkout_dest)
                })
            }
        }
    }
}

@Composable
fun CartScreen(onCheckout: () -> Unit) {
    // cartItems as mutable state so removals recompose the list immediately
    var cartItems by remember { mutableStateOf(ShoppingCart.items) }

    Column(Modifier.fillMaxSize().background(Color(0xFF1A1A2E))) {
        if (cartItems.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty.\nBrowse products to add items!", color = Color(0xFF6B7280), fontSize = 16.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
                items(cartItems, key = { it.product.id }) { item ->
                    CartItemRow(item = item, onRemove = {
                        ShoppingCart.removeItem(item.product.id)
                        cartItems = ShoppingCart.items
                    })
                }
            }
            Text(
                "Total: \$${"%.2f".format(ShoppingCart.totalPrice)}",
                color = Color(0xFFF5A623), fontWeight = FontWeight.Bold, fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
        Button(
            onClick = onCheckout,
            enabled = cartItems.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27AE60),
                disabledContainerColor = Color(0xFF27AE60).copy(alpha = 0.4f)
            )
        ) { Text("Proceed to Checkout", fontSize = 16.sp) }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${item.product.emoji} ${item.product.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Qty: ${item.quantity}", color = Color(0xFF9CA3AF), fontSize = 12.sp)
            }
            Text("\$${"%.2f".format(item.subtotal)}", color = Color(0xFFF5A623), fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 10.dp))
            Button(
                onClick = onRemove,
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
            ) { Text("Remove", fontSize = 11.sp) }
        }
    }
}
