package com.interviewprep.exercises.exercises.roulette.model

import com.interviewprep.exercises.core.ImmutableList

/**
 * RollResult — a single spin outcome
 *
 * @param number     the roulette number spun (0–36)
 * @param earnings   positive = won, negative = lost (in dollars)
 */
data class RollResult(
    val number: Int,
    val earnings: Int
) {
    val isWin: Boolean get() = earnings > 0
    val formattedEarnings: String get() =
        if (earnings >= 0) "+$$earnings" else "-$${Math.abs(earnings)}"
}

/**
 * BetOption — one row in the betting list
 *
 * @param label   display name, e.g. "Red", "Even", "1-12"
 * @param amount  current bet amount in dollars (0 = not betting)
 * @param payout  multiplier: win returns bet * payout
 */
data class BetOption(
    val id: Int,
    val label: String,
    val amount: Int = 0,
    val payout: Int = 2
)

/**
 * RouletteModel — holds the three independently-observable state fields.
 *
 * ─── Milestone 4 Q3: Why three separate LiveData fields, not one object? ────
 *
 * Each observer only cares about one slice of state:
 *  - HistoryFragment observes rollResults only — it doesn't need bet changes
 *  - BetFragment observes bets and totalMoney — it doesn't need roll history
 *
 * If we merged everything into one object, every bet increment would trigger
 * the history observer, causing unnecessary list re-renders. By separating
 * the fields, each fragment observes only what it needs — no wasted updates.
 *
 * ─── Milestone 4 Q2: Why ImmutableList for roll results? ────────────────────
 *
 * LiveData observers fire when the reference changes (not the contents).
 * If rollResults were a mutable list:
 *   list.add(result)   ← same reference, LiveData does NOT notify observers
 *
 * With ImmutableList, we must always create a new list to add an item:
 *   ImmutableList.builder().addAll(old).add(result).build()  ← new reference ✓
 *
 * This guarantees LiveData always notifies observers when the list changes.
 * Using a mutable list is a subtle bug that's easy to miss in code review.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
object RouletteModel {

    // Default bet options — representative subset of a real roulette table
    val defaultBetOptions: ImmutableList<BetOption> = ImmutableList.of(
        BetOption(id = 0, label = "Red",    payout = 2),
        BetOption(id = 1, label = "Black",  payout = 2),
        BetOption(id = 2, label = "Even",   payout = 2),
        BetOption(id = 3, label = "Odd",    payout = 2),
        BetOption(id = 4, label = "1–12",   payout = 3),
        BetOption(id = 5, label = "13–24",  payout = 3),
        BetOption(id = 6, label = "25–36",  payout = 3),
    )

    /**
     * Simulate a roulette spin and compute earnings from a bet list.
     * In a real app this logic lives in a Repository + UseCase layer.
     */
    fun spin(bets: ImmutableList<BetOption>): RollResult {
        val number = (0..36).random()
        val totalBet = bets.sumOf { it.amount }
        val totalWon = bets.filter { wonBet(it, number) }.sumOf { it.amount * it.payout }
        val earnings = totalWon - totalBet
        return RollResult(number = number, earnings = earnings)
    }

    private fun wonBet(bet: BetOption, number: Int): Boolean {
        if (number == 0) return false  // 0 loses all standard bets
        return when (bet.id) {
            0 -> number in redNumbers
            1 -> number !in redNumbers
            2 -> number % 2 == 0
            3 -> number % 2 != 0
            4 -> number in 1..12
            5 -> number in 13..24
            6 -> number in 25..36
            else -> false
        }
    }

    private val redNumbers = setOf(1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36)
}
