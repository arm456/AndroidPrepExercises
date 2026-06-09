package com.interviewprep.exercises.exercises.roulette.ui.bet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel
import com.interviewprep.exercises.exercises.roulette.model.BetOption
import com.interviewprep.exercises.core.ImmutableList

class BetFragment : Fragment() {

    private val viewModel: RouletteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { MaterialTheme { BetScreen(viewModel) } }
    }
}

@Composable
fun BetScreen(viewModel: RouletteViewModel) {
    // observeAsState bridges LiveData → Compose State
    val bets      by viewModel.bets.observeAsState(ImmutableList.of())
    val money     by viewModel.totalMoney.observeAsState(RouletteViewModel.STARTING_MONEY)
    val totalBet  by viewModel.totalBet.observeAsState(0)
    val canAddBet by viewModel.canAddBet.observeAsState(true)
    val canRoll   by viewModel.canRoll.observeAsState(false)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E))
    ) {
        // Money header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16213E))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Balance: \$$money", color = Color(0xFFF5A623), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Total Bet: \$$totalBet", color = Color(0xFF9CA3AF), fontSize = 14.sp)
        }

        Text(
            "← Swipe right for history",
            color = Color(0xFF6B7280), fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
        )

        // Bet options list — LazyColumn replaces RecyclerView
        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            itemsIndexed(bets.toList()) { _, bet ->
                BetOptionRow(
                    bet = bet,
                    canBet = canAddBet,
                    onIncrement = { viewModel.incrementBet(bet.id) }
                )
            }
        }

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16213E))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.clearBets() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151)),
                modifier = Modifier.weight(1f)
            ) { Text("Clear Bets") }

            Button(
                onClick = { viewModel.roll() },
                enabled = canRoll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5A623),
                    disabledContainerColor = Color(0xFFF5A623).copy(alpha = 0.4f)
                ),
                modifier = Modifier.weight(2f)
            ) { Text("ROLL", fontSize = 16.sp) }
        }
    }
}

@Composable
fun BetOptionRow(bet: BetOption, canBet: Boolean, onIncrement: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                bet.label,
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                if (bet.amount > 0) "\$${bet.amount}" else "\$0",
                color = Color(0xFFF5A623), fontSize = 14.sp,
                modifier = Modifier.padding(end = 12.dp).widthIn(min = 40.dp)
            )
            Button(
                onClick = onIncrement,
                enabled = canBet,
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFF4A90E2).copy(alpha = 0.4f)
                )
            ) { Text("+\$1", fontSize = 12.sp) }
        }
    }
}
