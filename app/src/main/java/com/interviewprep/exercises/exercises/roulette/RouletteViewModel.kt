package com.interviewprep.exercises.exercises.roulette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.interviewprep.exercises.core.ImmutableList
import com.interviewprep.exercises.exercises.roulette.model.BetOption
import com.interviewprep.exercises.exercises.roulette.model.RollResult
import com.interviewprep.exercises.exercises.roulette.model.RouletteModel

/**
 * RouletteViewModel
 *
 * Three separately-observable fields (Milestone 4 Q3):
 *   _totalMoney   → BetFragment (balance display, over-budget guard)
 *   _bets         → BetFragment (bet amount per row)
 *   _rollResults  → HistoryFragment only (never re-renders on bet changes)
 *
 * ─── Two distinct enabled states (Bug fix) ───────────────────────────────────
 *
 * We expose TWO separate derived LiveData values for the UI:
 *
 *   canAddBet: Boolean
 *     True when the user can still press +$1 on any row.
 *     False ONLY when totalBet >= totalMoney (over-budget — Milestone 3).
 *     Starts TRUE so buttons are usable immediately.
 *
 *   canRoll: Boolean
 *     True when totalBet > 0 AND totalBet <= totalMoney.
 *     Controls only the ROLL button.
 *
 * The previous bug conflated these two into one flag. canRoll starts false
 * (no bets placed yet), which was incorrectly used to disable the +$1 buttons
 * too — creating a deadlock where you couldn't add a bet to make canRoll true.
 *
 * ─── MediatorLiveData for both derived values ─────────────────────────────────
 * Both canAddBet and canRoll depend on _bets AND _totalMoney.
 * MediatorLiveData recomputes whenever either source changes.
 */
class RouletteViewModel : ViewModel() {

    companion object {
        const val STARTING_MONEY = 100  // Change to 10 to test Milestone 3 quickly
    }

    private val _totalMoney = MutableLiveData(STARTING_MONEY)
    val totalMoney: LiveData<Int> = _totalMoney

    private val _bets = MutableLiveData<ImmutableList<BetOption>>(RouletteModel.defaultBetOptions)
    val bets: LiveData<ImmutableList<BetOption>> = _bets

    private val _rollResults = MutableLiveData<ImmutableList<RollResult>>(ImmutableList.of())
    val rollResults: LiveData<ImmutableList<RollResult>> = _rollResults

    val totalBet: LiveData<Int> = _bets.map { list -> list.sumOf { it.amount } }

    /**
     * canAddBet — controls the +$1 buttons in each row.
     *
     * False ONLY when over-budget (totalBet >= totalMoney).
     * Starts TRUE — buttons are immediately usable.
     *
     * Interview note: this is Milestone 3's guard. Without it, the user could
     * keep betting past their balance and go negative after a roll.
     */
    val canAddBet: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        value = true  // ← start enabled so +$1 buttons work immediately
        fun recompute() {
            val bet = _bets.value?.sumOf { it.amount } ?: 0
            val money = _totalMoney.value ?: 0
            value = bet < money  // strictly less than — user can add one more dollar
        }
        addSource(_bets)       { recompute() }
        addSource(_totalMoney) { recompute() }
    }

    /**
     * canRoll — controls only the ROLL button.
     *
     * True when at least $1 is bet AND total bet doesn't exceed balance.
     * Starts FALSE (no bets placed) — ROLL button is disabled until user bets.
     */
    val canRoll: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        value = false  // ← start disabled; user must place a bet first
        fun recompute() {
            val bet = _bets.value?.sumOf { it.amount } ?: 0
            val money = _totalMoney.value ?: 0
            value = bet > 0 && bet <= money
        }
        addSource(_bets)       { recompute() }
        addSource(_totalMoney) { recompute() }
    }

    fun incrementBet(betId: Int) {
        val currentBets = _bets.value ?: return
        val currentMoney = _totalMoney.value ?: 0
        if (currentBets.sumOf { it.amount } >= currentMoney) return

        _bets.value = ImmutableList.copyOf(
            currentBets.map { bet ->
                if (bet.id == betId) bet.copy(amount = bet.amount + 1) else bet
            }
        )
    }

    fun clearBets() {
        _bets.value = ImmutableList.copyOf(
            (_bets.value ?: ImmutableList.of()).map { it.copy(amount = 0) }
        )
    }

    fun roll() {
        val currentBets = _bets.value ?: return
        if (currentBets.sumOf { it.amount } == 0) return

        val result = RouletteModel.spin(currentBets)
        _totalMoney.value = (_totalMoney.value ?: 0) + result.earnings

        val currentHistory = _rollResults.value ?: ImmutableList.of()
        _rollResults.value = ImmutableList.builder<RollResult>()
            .addAll(currentHistory)
            .add(result)
            .build()

        clearBets()
    }
}
