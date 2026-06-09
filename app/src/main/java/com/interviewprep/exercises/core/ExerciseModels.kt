package com.interviewprep.exercises.core

import androidx.annotation.IdRes
import com.interviewprep.exercises.R

// ─────────────────────────────────────────────
// Shared data model used across all exercises
// ─────────────────────────────────────────────

data class ExerciseItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val colorHex: String = "#4A90E2"
)

// ─────────────────────────────────────────────
// Exercise registry
// To add a new exercise:
//   Option A — Fragment: fill destinationId, leave activityClass null
//   Option B — Activity: fill activityClass, leave destinationId null
// ─────────────────────────────────────────────

data class Exercise(
    val id: String,
    val title: String,
    val description: String,
    @IdRes val destinationId: Int? = null,
    val activityClass: Class<*>? = null
)

object ExerciseRegistry {

    val all: List<Exercise> = listOf(
        Exercise(
            id = "horizontal_scroll_list",
            title = "01 · Horizontal Scroll — RecyclerView",
            description = "Multi-item scroll. Center detection via findFirstCompletelyVisibleItemPosition().",
            destinationId = R.id.horizontalListFragment
        ),
        Exercise(
            id = "horizontal_scroll_pager",
            title = "01 · Horizontal Scroll — ViewPager2",
            description = "One-at-a-time snap. Center detection via getCurrentItem() / onPageSelected().",
            destinationId = R.id.horizontalPagerFragment
        ),
        Exercise(
            id = "roulette",
            title = "02 · Roulette Case Study",
            description = "LiveData, ImmutableList, shared ViewModel, ViewPager2, over-budget guard.",
            destinationId = R.id.rouletteMainFragment
        ),
        Exercise(
            id = "shopping_navigation",
            title = "03 · Jetpack Navigation — Shopping Cart",
            description = "NavGraph, NavController, Safe Args, deep links, bottom nav, drawer, popUpTo.",
            activityClass = com.interviewprep.exercises.exercises.shopping.ShoppingActivity::class.java
        )
        // ➕ Add new exercises here — they appear automatically on the home screen
    )

    // Sample dataset shared across Exercise 01 horizontal scroll implementations
    val sampleExerciseItems: List<ExerciseItem> = listOf(
        ExerciseItem(1, "Inbox",     "12 messages",  "Your primary inbox",          "#4A90E2"),
        ExerciseItem(2, "Starred",   "3 messages",   "Messages you starred",        "#F5A623"),
        ExerciseItem(3, "Sent",      "47 messages",  "Messages you sent",           "#7ED321"),
        ExerciseItem(4, "Drafts",    "2 messages",   "Unsent drafts",               "#9B59B6"),
        ExerciseItem(5, "Spam",      "0 messages",   "No spam — lucky you",         "#E74C3C"),
        ExerciseItem(6, "Trash",     "5 messages",   "Deleted messages",            "#95A5A6"),
        ExerciseItem(7, "Archive",   "128 messages", "Archived conversations",      "#1ABC9C"),
    )
}
