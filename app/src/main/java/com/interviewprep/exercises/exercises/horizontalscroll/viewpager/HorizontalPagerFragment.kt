package com.interviewprep.exercises.exercises.horizontalscroll.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.interviewprep.exercises.core.ExerciseRegistry
import com.interviewprep.exercises.exercises.horizontalscroll.HorizontalScrollViewModel
import com.interviewprep.exercises.exercises.horizontalscroll.recyclerview.FooterBar
import com.interviewprep.exercises.exercises.horizontalscroll.recyclerview.HeaderBar
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.core.graphics.toColorInt

/**
 * HorizontalPagerFragment — Approach B: Compose HorizontalPager (replaces ViewPager2)
 *
 * ─── XML → Compose mapping ───────────────────────────────────────────────────
 *   ViewPager2                   →  HorizontalPager (foundation-pager)
 *   FragmentStateAdapter         →  content lambda inside HorizontalPager { page -> }
 *   OnPageChangeCallback         →  LaunchedEffect + snapshotFlow { pagerState.currentPage }
 *   getCurrentItem()             →  pagerState.currentPage
 *
 * ─── Key advantage over ViewPager2 ───────────────────────────────────────────
 * HorizontalPager requires no Adapter class at all. Each page is just a
 * composable lambda — ExercisePageContent() called inline. The outer/inner
 * Fragment split from the ViewPager2 approach collapses into one composable
 * function with a pagerState, which is architecturally cleaner.
 */
class HorizontalPagerFragment : Fragment() {

    private val viewModel: HorizontalScrollViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                HorizontalPagerScreen(viewModel)
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerScreen(viewModel: HorizontalScrollViewModel) {
    val selectedItem by viewModel.selectedItem.observeAsState(
        ExerciseRegistry.sampleExerciseItems.first()
    )
    val items = ExerciseRegistry.sampleExerciseItems

    // PagerState replaces ViewPager2's internal state + getCurrentItem()
    val pagerState = rememberPagerState(pageCount = { items.size })

    // Observe page changes — replaces OnPageChangeCallback.onPageSelected()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page -> viewModel.onItemCentered(page) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        HeaderBar(
            title = "📂  ${selectedItem.title}",
            label = "APPROACH B — HorizontalPager (snap)"
        )

        Spacer(Modifier.weight(1f))

        // HorizontalPager replaces ViewPager2 + FragmentStateAdapter entirely.
        // Each page is rendered inline — no separate Fragment or Adapter class needed.
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            ExercisePageContent(item = items[page])
        }

        // Page indicator — replaces the tvPageIndicator TextView
        Text(
            text = "${pagerState.currentPage + 1} / ${items.size}",
            fontSize = 13.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        )
        Text(
            text = "Swipe one at a time. Header/footer update on snap.",
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        Spacer(Modifier.weight(1f))

        FooterBar(description = selectedItem.description)
    }
}

@Composable
fun ExercisePageContent(item: com.interviewprep.exercises.core.ExerciseItem) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(item.colorHex.toColorInt()))
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1A1A2E))
                Text(item.subtitle, fontSize = 14.sp, color = Color(0xFF6B7280), modifier = Modifier.padding(top = 6.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp).width(40.dp), thickness = 2.dp, color = Color(0xFFE5E7EB))
                Text(item.description, fontSize = 13.sp, color = Color(0xFF4B5563))
            }
        }
    }
}
