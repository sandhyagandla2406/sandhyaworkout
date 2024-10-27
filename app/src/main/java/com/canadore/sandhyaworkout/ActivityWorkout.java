package com.canadore.sandhyaworkout;

import com.canadore.sandhyaworkout.Workout; // Adjust the package name if necessary

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.canadore.sandhyaworkout.databinding.ActivityWorkoutBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ActivityWorkout extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ActivityWorkoutBinding binding;
    private ArrayList<Workout> workoutList;
    private ArrayAdapter<Workout> workoutAdapter;

    // Define request code for updating workouts
    private static final int UPDATE_WORKOUT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("workouts");

        // Initialize the workout list and adapter
        workoutList = new ArrayList<>();
        workoutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutList);
        binding.workoutListView.setAdapter(workoutAdapter);

        // Button click listener for adding workout
        binding.addButton.setOnClickListener(v -> addWorkout());

        // Set an OnItemClickListener for the ListView
        binding.workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout selectedWorkout = workoutList.get(position);
                Intent intent = new Intent(ActivityWorkout.this, ActivityUpdateDelete.class);
                // Pass the workout data to the next activity (you might want to pass the ID)
                intent.putExtra("WORKOUT_ID", selectedWorkout.getId());
                intent.putExtra("WORKOUT_NAME", selectedWorkout.getName());
                intent.putExtra("WORKOUT_DURATION", selectedWorkout.getDuration());
                intent.putExtra("WORKOUT_CALORIES", selectedWorkout.getCalories());
                startActivityForResult(intent, UPDATE_WORKOUT_REQUEST_CODE);
            }
        });
    }

    // Add workout method
    private void addWorkout() {
        String workoutId = databaseReference.push().getKey();
        String name = binding.workoutNameEditText.getText().toString().trim();
        String durationStr = binding.workoutDurationEditText.getText().toString().trim();
        String caloriesStr = binding.workoutCaloriesEditText.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || durationStr.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse duration and calories with error handling
        int duration;
        int calories;
        try {
            duration = Integer.parseInt(durationStr);
            calories = Integer.parseInt(caloriesStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for duration and calories", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Workout object
        Workout newWorkout = new Workout(workoutId, name, duration, calories);

        // Log the workout details after instantiation
        Log.d("AddWorkout", "Adding workout: " + newWorkout.toString());

        // Add workout to Firebase
        databaseReference.child(workoutId).setValue(newWorkout)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityWorkout.this, "Workout added successfully", Toast.LENGTH_SHORT).show();
                        workoutList.add(newWorkout);
                        workoutAdapter.notifyDataSetChanged();
                        clearInputFields();
                    } else {
                        Log.e("AddWorkout", "Failed to add workout", task.getException());
                        Toast.makeText(ActivityWorkout.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Clear input fields method
    private void clearInputFields() {
        binding.workoutNameEditText.setText("");
        binding.workoutDurationEditText.setText("");
        binding.workoutCaloriesEditText.setText("");
    }

    // Handle the result from the ActivityUpdateDelete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_WORKOUT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the updated workout from the result
            Workout updatedWorkout = (Workout) data.getSerializableExtra("UPDATED_WORKOUT");

            // Find the position of the workout in the local list and update it
            for (int i = 0; i < workoutList.size(); i++) {
                if (workoutList.get(i).getId().equals(updatedWorkout.getId())) {
                    workoutList.set(i, updatedWorkout); // Update the workout
                    break;
                }
            }

            // Notify the adapter about the updated list
            workoutAdapter.notifyDataSetChanged();
        }
    }
}
