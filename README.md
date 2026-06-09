# Android Interview Prep Exercises

A modular Android codebase built for **interview preparation and reference** — each screen demonstrates a distinct Android concept with two implementation approaches, clean architecture, and inline commentary explaining *why* decisions are made.

> Built with Kotlin · Jetpack Compose-ready architecture · MVVM · Hilt-ready structure

---

## 📋 Exercise Index

| # | Exercise | Key Concepts |
|---|----------|-------------|
| 01A | [Horizontal Scroll — RecyclerView](#exercise-01-horizontal-scroll) | Multi-item fling, `findFirstCompletelyVisibleItemPosition`, scroll listener |
| 01B | [Horizontal Scroll — ViewPager2](#exercise-01-horizontal-scroll) | Snap scroll, `onPageSelected`, shared ViewModel cross-fragment update |
| 02 | [Roulette — Case Study](#exercise-02-roulette-case-study) | LiveData, ImmutableList, MediatorLiveData, shared ViewModel, over-budget guard |
| 03 | [Jetpack Navigation — Shopping Cart](#exercise-03-jetpack-navigation--shopping-cart) | NavGraph, NavController, Safe Args, deep links, bottom nav, drawer, `popUpTo` |

---

## Exercise 01: Horizontal Scroll

### Problem Statement

> Design a screen that horizontally scrolls like selecting an email in Gmail. Evaluate two approaches and explain: (1) how to scroll multiple items vs. snap one-by-one, (2) how to detect which item is "centered", and (3) how to update top/bottom UI to match the centered item.

---

### Approach A — RecyclerView (multi-scroll)

**When to use:** User can fling and land on any item. Good for browsing-style UIs (photo galleries, carousels where position doesn't strictly matter).

**Key classes:**
- `HorizontalListFragment` — hosts the RecyclerView
- `ExerciseCardAdapter` — RecyclerView.Adapter with DiffUtil
- `HorizontalScrollViewModel` — exposes `selectedItem: LiveData<ExerciseItem>`

**How center detection works:**
```kotlin
// LinearLayoutManager gives us this for free:
val centerPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

// Attach a scroll listener:
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val pos = layoutManager.findFirstCompletelyVisibleItemPosition()
            viewModel.onItemCentered(pos)
        }
    }
})
```

**How UI updates work:**
- ViewModel holds `selectedItem: MutableLiveData<ExerciseItem>`
- Header and footer observe this LiveData and re-render on change
- Everything stays in one Fragment — no inter-fragment communication needed

---

### Approach B — ViewPager2 (snap, one-by-one)

**When to use:** Each item is a "page" — strict one-at-a-time navigation. Good for onboarding flows, step-by-step exercises, or Gmail-style email swipe.

**Key classes:**
- `HorizontalPagerFragment` — hosts the ViewPager2 (outer shell)
- `ExercisePageFragment` — one Fragment per item (inner page)
- `ExercisePagerAdapter` — FragmentStateAdapter
- `HorizontalScrollViewModel` — shared ViewModel between outer and page fragments

**How center detection works:**
```kotlin
// ViewPager2 has getCurrentItem():
val currentPosition = viewPager.currentItem  // always the "snapped" page

// Or observe via callback:
viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        viewModel.onItemCentered(position)
    }
})
```

**How UI updates work:**
- `HorizontalPagerFragment` observes the shared ViewModel
- Header/footer live in `HorizontalPagerFragment`, not inside page fragments
- Page fragments post updates *up* via the shared ViewModel (scoped to Activity or NavGraph)
- This is the cleanest pattern: **ViewModel + LiveData replaces callbacks and interfaces**

---

### Trade-off Summary

| Concern | RecyclerView | ViewPager2 |
|---------|-------------|------------|
| Scroll behavior | Multi-item fling | Strict one-at-a-time snap |
| Center detection | `findFirstCompletelyVisibleItemPosition()` | `getCurrentItem()` or `onPageSelected` |
| UI update mechanism | Scroll listener → ViewModel | `OnPageChangeCallback` → ViewModel |
| Fragment structure | Single fragment | Outer + one fragment per page |
| Best for | Galleries, carousels | Onboarding, step-by-step, email swipe |

---

## Project Structure

```
app/src/main/java/com/interviewprep/exercises/
├── core/
│   ├── BaseFragment.kt          # Common fragment setup
│   ├── ExerciseItem.kt          # Shared data model
│   └── ExerciseRegistry.kt      # Central registry — add new exercises here
├── exercises/
│   └── horizontalscroll/
│       ├── HorizontalScrollViewModel.kt   # Shared ViewModel for both approaches
│       ├── recyclerview/
│       │   ├── HorizontalListFragment.kt
│       │   └── ExerciseCardAdapter.kt
│       └── viewpager/
│           ├── HorizontalPagerFragment.kt
│           ├── ExercisePageFragment.kt
│           └── ExercisePagerAdapter.kt
└── MainActivity.kt
```

---

## ➕ Adding a New Exercise

1. Create a new package under `exercises/` (e.g., `exercises/dragdrop/`)
2. Add your Fragment(s) and ViewModel there
3. Register it in `ExerciseRegistry.kt`:

```kotlin
Exercise(
    id = "drag_drop",
    title = "Drag & Drop",
    description = "Long-press to reorder list items",
    destinationId = R.id.dragDropFragment
)
```

4. Add the Fragment to `nav_graph.xml`
5. Done — it appears automatically on the home screen

---

## Concepts Covered (by exercise)

### Exercise 01
- `RecyclerView` with `LinearLayoutManager(HORIZONTAL)`
- `SnapHelper` vs no snap
- `RecyclerView.OnScrollListener` + `findFirstCompletelyVisibleItemPosition()`
- `ViewPager2` with `FragmentStateAdapter`
- `ViewPager2.OnPageChangeCallback`
- Shared `ViewModel` scoped to Activity for cross-fragment communication
- `LiveData` observation for reactive UI updates
- `DiffUtil` for efficient RecyclerView updates

---

## Interview Q&A Cheat Sheet

**Q: What's the difference between RecyclerView and ViewPager2 for horizontal scrolling?**  
A: RecyclerView allows free-form scrolling across multiple items; ViewPager2 enforces one-page-at-a-time snapping. ViewPager2 is actually built on RecyclerView internally.

**Q: How do you detect the center item in a horizontal RecyclerView?**  
A: Use `LinearLayoutManager.findFirstCompletelyVisibleItemPosition()` inside an `OnScrollListener` triggered when scroll state becomes `IDLE`.

**Q: How do you update header/footer UI from a ViewPager2 page change?**  
A: The cleanest way is a shared ViewModel scoped to the activity or nav graph. Page fragments write to it; the outer fragment observes it. Avoids callbacks and tight coupling.

**Q: Why use a shared ViewModel instead of an interface callback?**  
A: ViewModels survive configuration changes, are lifecycle-aware, and decouple the producer (page fragment) from the consumer (outer fragment). Interfaces create tight coupling and leak references if not cleared.

---

## Tech Stack

- Language: **Kotlin**
- Min SDK: 24 | Target SDK: 34
- Architecture: **MVVM**
- Navigation: **Jetpack Navigation Component**
- Views: **XML layouts** (Compose-ready structure)
- DI: structured for **Hilt** (annotations stubbed)

---

## License

MIT — free to use for interview prep, portfolio projects, and internal eng onboarding.

---

## Exercise 02: Roulette — Case Study

### Problem Statement

> You just joined a team working on a Roulette app. Implement a history fragment accessible by swiping right. The fragment should refresh automatically when bets are updated. Complete 4 milestones covering list UI, inter-fragment sync, budget validation, and architectural review.

---

### Milestone 1 — History Fragment List

Build a `HistoryFragment` that shows each roll result: the number rolled and the earnings (green for win, red for loss).

**Key class:** `HistoryAdapter` — `ListAdapter<RollResult>` with DiffUtil. Renders each `RollResult` with color-coded earnings.

```kotlin
data class RollResult(
    val number: Int,
    val earnings: Int
) {
    val formattedEarnings: String get() =
        if (earnings >= 0) "+$$earnings" else "-$${Math.abs(earnings)}"
}
```

---

### Milestone 2 — Auto-sync Between Pages (No Pull-to-Refresh)

`HistoryFragment` auto-updates because it observes `rollResults: LiveData<ImmutableList<RollResult>>` from the **shared ViewModel** scoped to the Activity.

When `BetFragment` calls `viewModel.roll()`:
1. ViewModel computes result, updates `_rollResults` with a new `ImmutableList` reference
2. LiveData fires the observer in `HistoryFragment`
3. `adapter.submitList()` diffs and animates the new row

No polling. No callbacks. No manual refresh.

---

### Milestone 3 — Disable Betting When Over Budget

```kotlin
val canRoll: LiveData<Boolean> = _bets.map { list ->
    val bet = list.sumOf { it.amount }
    bet > 0 && bet <= (_totalMoney.value ?: 0)
}
```

`BetFragment` observes `canRoll`. When `false`:
- The ROLL button is disabled + dimmed (alpha 0.4)
- All +$1 row buttons are disabled via `adapter.setBettingEnabled(false)`

> **Quick test:** Change `STARTING_MONEY = 100` to `10` in `RouletteViewModel`.

---

### Milestone 4 — Architectural Review (Q&A)

**Q1: How many fragments were added and why? Can there be fewer?**

3 fragments added:
- `MainFragment` — required outer container for `ViewPager2` (pager needs one Fragment per page)
- `BetFragment` — left page, betting UI
- `HistoryFragment` — right page, roll history

Could there be fewer? Yes. If we restructured to use `SlidingPaneLayout` or a single fragment with a split layout, `BetFragment` and `HistoryFragment` could merge into one, and `MainFragment` would be unnecessary. Minimum: **1 fragment** (or 0 with a pure Activity).

---

**Q2: Why store roll results as `ImmutableList`?**

LiveData uses **reference equality** to detect changes. If we used a mutable list:
```kotlin
list.add(result)  // same reference → LiveData does NOT notify observers ❌
```

With `ImmutableList`, we must always build a new list:
```kotlin
ImmutableList.builder<RollResult>().addAll(old).add(result).build()  // new reference ✓
```

This guarantees LiveData always fires when the list changes. Using a mutable list is a subtle, hard-to-debug bug that fails silently — the UI just never updates.

---

**Q3: Should there be one combined observable object or three separate LiveData fields?**

Three separate fields — intentionally:

| Field | Observed by | Why separate |
|-------|-------------|-------------|
| `_totalMoney` | BetFragment | Balance display |
| `_bets` | BetFragment | Bet amount display + canRoll |
| `_rollResults` | HistoryFragment | Roll history list |

If we merged into one `UiState`, every `+$1` bet increment would trigger `HistoryFragment`'s observer — causing unnecessary list re-diffs with no visible change. Fine-grained observation means each fragment only reacts to state it actually cares about. This is a meaningful performance and correctness concern at scale.

---

### Fragment Architecture Diagram

```
MainActivity
└── NavHostFragment
    └── MainFragment (outer ViewPager2 host)
        ├── [Page 0] BetFragment      — betting table, observes: bets, totalMoney, canRoll
        └── [Page 1] HistoryFragment  — roll history, observes: rollResults only

    Shared: RouletteViewModel (activityViewModels scope)
              ├── _totalMoney: MutableLiveData<Int>
              ├── _bets: MutableLiveData<ImmutableList<BetOption>>
              └── _rollResults: MutableLiveData<ImmutableList<RollResult>>
```


---

## Exercise 03: Jetpack Navigation — Shopping Cart

### Problem Statement

> Prototype a shopping cart application demonstrating all core Jetpack Navigation Component patterns. Milestone 1 covers the full navigation codelab (NavGraph, NavController, Safe Args, animations, deep links, menu navigation). Milestone 2 adds an extra navigation entry point to the shopping cart on the home screen.

---

### Navigation Component Architecture

```
ShoppingActivity  (single Activity host)
  ├── Toolbar          ← label auto-set by NavigationUI from nav graph
  ├── NavHostFragment  ← swaps fragment destinations
  ├── BottomNavigationView  ← wired via NavigationUI.setupWithNavController
  └── DrawerLayout + NavigationView  ← wired via NavigationUI.setupWithNavController

shopping_navigation.xml  (the nav graph)
  ├── shop_home_dest      (HomeFragment)    ← start destination
  ├── shop_product_dest   (ProductDetailFragment)  ← argument: productId
  ├── shop_cart_dest      (CartFragment)    ← top-level + deep link
  ├── shop_checkout_dest  (CheckoutFragment)
  └── shop_settings_dest  (SettingsFragment) ← overflow menu target
```

---

### Milestone 1 — Navigation Codelab Concepts Implemented

**NavGraph + NavController**
```kotlin
// In ShoppingActivity:
val navHostFragment = supportFragmentManager
    .findFragmentById(R.id.shopping_nav_host_fragment) as NavHostFragment
navController = navHostFragment.navController
```

**NavigationUI wiring** — zero manual item-click handlers needed:
```kotlin
navView.setupWithNavController(navController)        // drawer
bottomNav.setupWithNavController(navController)      // bottom nav
setupActionBarWithNavController(navController, appBarConfig)  // toolbar title
```

**AppBarConfiguration** — controls hamburger vs back arrow:
```kotlin
appBarConfiguration = AppBarConfiguration(
    topLevelDestinationIds = setOf(R.id.shop_home_dest, R.id.shop_cart_dest),
    drawerLayout = drawerLayout
)
// Home and Cart show ≡ (hamburger). Product Detail, Checkout show ← (back).
```

**Navigate by action ID with argument (Safe Args equivalent):**
```kotlin
// HomeFragmentDirections manually mirrors what the Safe Args plugin generates:
val action = HomeFragmentDirections.actionHomeToProductDest(product.id)
findNavController().navigate(action)

// Reading in ProductDetailFragment:
val productId = arguments?.getInt("productId")
```

**Slide animations** — defined in nav graph action, applied automatically:
```xml
<action android:id="@+id/action_home_to_product_dest"
    app:destination="@id/shop_product_dest"
    app:enterAnim="@anim/shop_slide_in_right"
    app:exitAnim="@anim/shop_slide_out_left"
    app:popEnterAnim="@anim/shop_slide_in_left"
    app:popExitAnim="@anim/shop_slide_out_right"/>
```

**Menu navigation** (overflow ⋮ → Settings):
```kotlin
// Item ID "shop_settings_dest" matches destination ID in nav graph.
// NavigationUI handles the navigate() call automatically:
NavigationUI.onNavDestinationSelected(menuItem, findNavController())
```

**popUpTo / popUpToInclusive** — back stack after checkout:
```xml
<action android:id="@+id/action_cart_to_checkout_dest"
    app:destination="@id/shop_checkout_dest"
    app:popUpTo="@id/shop_home_dest"
    app:popUpToInclusive="false"/>
<!-- Result: Checkout → Back → Home (skips Cart, which is now stale) -->
```

**Deep link** — external apps can open cart directly:
```xml
<fragment android:id="@+id/shop_cart_dest" ...>
    <deepLink app:uri="www.shoppingapp.example.com/cart"/>
</fragment>
```

---

### Milestone 2 — Extra Cart Navigation Entry Point

Three ways to reach CartFragment:

| Path | Mechanism |
|------|-----------|
| Bottom navigation bar "Cart" icon | `NavigationUI.setupWithNavController` auto-wired |
| Navigation drawer "My Cart" item | `NavigationUI.setupWithNavController` auto-wired |
| **Home screen FAB button (Milestone 2)** | `findNavController().navigate(R.id.shop_cart_dest)` |

The FAB on `HomeFragment` is the designer's requested addition. It also shows a badge with the current item count.

---

### Safe Args — With vs Without Plugin

| | With Safe Args Plugin | This Reference Project |
|---|---|---|
| Code generation | Gradle generates `HomeFragmentDirections` | Written manually in `HomeFragmentDirections.kt` |
| Argument reading | `val args by navArgs<ProductDetailFragmentArgs>()` | `arguments?.getInt("productId")` |
| Type safety | Compile-time | Runtime |
| Build complexity | Requires kapt + plugin in both build.gradle files | None |

The runtime behavior is identical. Add the plugin for real projects; the manual approach is clear enough for a reference repo.

