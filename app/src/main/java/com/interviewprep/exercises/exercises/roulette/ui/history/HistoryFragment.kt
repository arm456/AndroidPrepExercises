package com.interviewprep.exercises.exercises.roulette.ui.history

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.interviewprep.exercises.core.ImmutableList
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel
import com.interviewprep.exercises.exercises.roulette.model.RollResult

class HistoryFragment : Fragment() {

    private val viewModel: RouletteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { MaterialTheme { HistoryScreen(viewModel) } }
    }
}

@Composable
fun HistoryScreen(viewModel: RouletteViewModel) {
    val results by viewModel.rollResults.observeAsState(ImmutableList.of())
    val list = results.toList().reversed()

    Column(Modifier.fillMaxSize().background(Color(0xFF1A1A2E))) {
        Text(
            "Roll History",
            color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16213E))
                .padding(16.dp)
        )
        Text(
            "Swipe left to bet →",
            color = Color(0xFF6B7280), fontSize = 11.sp,
            modifier = Modifier.padding(8.dp)
        )

        if (list.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No rolls yet.\nPlace a bet and spin!",
                    color = Color(0xFF6B7280), fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(list) { result -> RollResultRow(result) }
            }
        }
    }
}

@Composable
fun RollResultRow(result: RollResult) {
    val earningsColor = when {
        result.earnings > 0 -> Color(0xFF27AE60)
        result.earnings < 0 -> Color(0xFFE74C3C)
        else                -> Color(0xFF95A5A6)
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rolled: ${result.number}", color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
            Text(result.formattedEarnings, color = earningsColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
