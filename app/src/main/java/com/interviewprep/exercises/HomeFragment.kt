package com.interviewprep.exercises

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.interviewprep.exercises.core.Exercise
import com.interviewprep.exercises.core.ExerciseRegistry

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                ExerciseListScreen(
                    exercises = ExerciseRegistry.all,
                    onLaunch = { exercise ->
                        when {
                            exercise.activityClass != null ->
                                startActivity(Intent(requireContext(), exercise.activityClass))
                            exercise.destinationId != null ->
                                findNavController().navigate(exercise.destinationId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseListScreen(exercises: List<Exercise>, onLaunch: (Exercise) -> Unit) {
//    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(exercises) { exercise ->
            ExerciseEntryCard(exercise = exercise, onLaunch = { onLaunch(exercise) })
        }
    }
}

@Composable
fun ExerciseEntryCard(exercise: Exercise, onLaunch: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(exercise.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A2E))
            Text(
                exercise.description,
                fontSize = 13.sp, color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            Button(onClick = onLaunch) { Text("Launch Exercise", fontSize = 13.sp) }
        }
    }
}
