package com.interviewprep.exercises.exercises.roulette.ui.bet

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interviewprep.exercises.R
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.exercises.roulette.RouletteViewModel

/**
 * BetFragment — Milestone 1, 2, 3
 *
 * Observes two distinct LiveData values from the ViewModel:
 *
 *   canAddBet → controls the +$1 buttons in each row
 *               false only when totalBet >= totalMoney (over-budget)
 *               starts TRUE so buttons work immediately on launch
 *
 *   canRoll   → controls only the ROLL button
 *               false until at least $1 is bet
 */
class BetFragment : BaseFragment(R.layout.fragment_bet) {

    private val viewModel: RouletteViewModel by activityViewModels()
    private lateinit var adapter: BetAdapter
    private lateinit var btnRoll: Button
    private lateinit var btnClear: Button
    private lateinit var tvTotalMoney: TextView
    private lateinit var tvTotalBet: TextView

    override fun onViewReady(savedInstanceState: Bundle?) {
        bindViews()
        setupRecyclerView()
        observeViewModel()
    }

    private fun bindViews() {
        btnRoll  = requireView().findViewById(R.id.btnRoll)
        btnClear = requireView().findViewById(R.id.btnClearBets)
        tvTotalMoney = requireView().findViewById(R.id.tvTotalMoney)
        tvTotalBet   = requireView().findViewById(R.id.tvTotalBet)

        btnRoll.setOnClickListener  { viewModel.roll() }
        btnClear.setOnClickListener { viewModel.clearBets() }

        // ROLL starts disabled — user must place a bet first
        btnRoll.isEnabled = false
        btnRoll.alpha = 0.4f
    }

    private fun setupRecyclerView() {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerViewBets)
        adapter = BetAdapter(
            onIncrementClicked = { betId -> viewModel.incrementBet(betId) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.bets.observe(viewLifecycleOwner) { bets ->
            adapter.submitList(bets.toList())
        }

        viewModel.totalMoney.observe(viewLifecycleOwner) { money ->
            tvTotalMoney.text = "Balance: $$money"
        }

        viewModel.totalBet.observe(viewLifecycleOwner) { bet ->
            tvTotalBet.text = "Total Bet: $$bet"
        }

        // +$1 buttons: disabled only when over-budget (Milestone 3)
        // Starts true — user can immediately place bets
        viewModel.canAddBet.observe(viewLifecycleOwner) { canAdd ->
            adapter.setBettingEnabled(canAdd)
        }

        // ROLL button: disabled until at least $1 is bet
        viewModel.canRoll.observe(viewLifecycleOwner) { canRoll ->
            btnRoll.isEnabled = canRoll
            btnRoll.alpha = if (canRoll) 1.0f else 0.4f
        }
    }
}
