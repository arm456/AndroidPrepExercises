package com.interviewprep.exercises.exercises.shopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(requireContext()).apply {
            setContent { MaterialTheme { SettingsScreen() } }
        }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⚙️", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text("Settings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "Reached via overflow menu (⋮).\n\nNavigationUI.onNavDestinationSelected()\nmatched the menu item ID 'shop_settings_dest'\nto this destination automatically.",
            color = Color(0xFF6B7280), fontSize = 14.sp, textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
