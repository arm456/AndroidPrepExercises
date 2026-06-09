package com.interviewprep.exercises.exercises.horizontalscroll.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.interviewprep.exercises.core.ExerciseItem
import com.interviewprep.exercises.core.ExerciseRegistry
import com.interviewprep.exercises.exercises.horizontalscroll.HorizontalScrollViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.core.graphics.toColorInt

/**
 * HorizontalListFragment — Approach A: Compose LazyRow (replaces RecyclerView)
 *
 * ─── XML → Compose mapping ───────────────────────────────────────────────────
 *   RecyclerView (horizontal)  →  LazyRow
 *   LinearLayoutManager        →  built into LazyRow
 *   ViewHolder + Adapter       →  items { } lambda in LazyRow
 *   OnScrollListener (IDLE)    →  snapshotFlow { listState.firstVisibleItemIndex }
 *                                 + .distinctUntilChanged() in a LaunchedEffect
 *
 * ─── Center detection ────────────────────────────────────────────────────────
 * LazyListState.firstVisibleItemIndex replaces
 * LinearLayoutManager.findFirstCompletelyVisibleItemPosition().
 * We collect it via snapshotFlow inside a LaunchedEffect so it only fires
 * when the index actually changes (equivalent to SCROLL_STATE_IDLE behaviour).
 */
class HorizontalListFragment : Fragment() {

    private val viewModel: HorizontalScrollViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                HorizontalListScreen(viewModel)
            }
        }
    }
}

@Composable
fun HorizontalListScreen(viewModel: HorizontalScrollViewModel) {
    val selectedItem by viewModel.selectedItem.observeAsState(
        ExerciseRegistry.sampleExerciseItems.first()
    )
    val items = ExerciseRegistry.sampleExerciseItems

    // LazyListState holds scroll position and exposes firstVisibleItemIndex
    val listState = rememberLazyListState()

    // snapshotFlow converts Compose state into a Flow so we can react to changes.
    // This is the Compose equivalent of RecyclerView.OnScrollListener.
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()   // equivalent to checking SCROLL_STATE_IDLE
            .collect { index -> viewModel.onItemCentered(index) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // Header — updates reactively when selectedItem changes
        HeaderBar(title = "📂  ${selectedItem.title}", label = "APPROACH A — LazyRow (multi-scroll)")

        Spacer(Modifier.weight(1f))

        // LazyRow replaces horizontal RecyclerView + LinearLayoutManager
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(160.dp)
        ) {
            items(items.size) { index ->
                ExerciseCard(item = items[index])
            }
        }

        Text(
            text = "Scroll freely. Header/footer update on scroll.",
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        Spacer(Modifier.weight(1f))

        // Footer — updates reactively
        FooterBar(description = selectedItem.description)
    }
}

@Composable
fun HeaderBar(title: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF9CA3AF), modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun FooterBar(description: String) {
    Text(
        text = description,
        fontSize = 14.sp,
        color = Color(0xFF4B5563),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    )
}

@Composable
fun ExerciseCard(item: ExerciseItem) {
    Card(
        modifier = Modifier.width(140.dp).fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(item.colorHex.toColorInt()))
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A2E))
                Text(item.subtitle, fontSize = 11.sp, color = Color(0xFF6B7280), modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
