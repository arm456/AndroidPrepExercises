package com.interviewprep.exercises

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.interviewprep.exercises.core.BaseFragment
import com.interviewprep.exercises.core.ExerciseRegistry

/**
 * HomeFragment — exercise list.
 *
 * Dynamically renders a card for each exercise in ExerciseRegistry.
 * Supports both Fragment destinations and Activity launches.
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override fun onViewReady(savedInstanceState: Bundle?) {
        val container = requireView().findViewById<LinearLayout>(R.id.exerciseListContainer)
        val navController = findNavController()

        ExerciseRegistry.all.forEach { exercise ->
            val card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_exercise_entry, container, false)

            card.findViewById<TextView>(R.id.tvExerciseTitle).text = exercise.title
            card.findViewById<TextView>(R.id.tvExerciseDescription).text = exercise.description

            card.findViewById<Button>(R.id.btnLaunch).setOnClickListener {
                when {
                    exercise.activityClass != null -> {
                        // Exercise is a standalone Activity (e.g. Exercise 03 Shopping)
                        startActivity(Intent(requireContext(), exercise.activityClass))
                    }
                    exercise.destinationId != null -> {
                        // Exercise is a Fragment destination in this nav graph
                        navController.navigate(exercise.destinationId)
                    }
                }
            }

            container.addView(card)
        }
    }
}
