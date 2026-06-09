package com.interviewprep.exercises.exercises.shopping.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.interviewprep.exercises.R
import com.interviewprep.exercises.exercises.shopping.model.ShoppingCart

class CheckoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val total = ShoppingCart.totalPrice
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    CheckoutScreen(
                        total = total,
                        onConfirm = {
                            ShoppingCart.clear()
                            findNavController().navigate(
                                R.id.shop_home_dest, null,
                                NavOptions.Builder().setPopUpTo(R.id.shop_home_dest, false).build()
                            )
                        },
                        onBack = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen(total: Double, onConfirm: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🛍️", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text("Review Your Order", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "Order Total: \$${"%.2f".format(total)}",
            color = Color(0xFFF5A623), fontWeight = FontWeight.Bold, fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60))
        ) { Text("Confirm Order ✓", fontSize = 16.sp) }
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151))
        ) { Text("← Back to Cart", fontSize = 14.sp) }
    }
}
