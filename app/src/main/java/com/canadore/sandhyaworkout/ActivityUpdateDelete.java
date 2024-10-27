package com.canadore.sandhyaworkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityUpdateDelete extends AppCompatActivity {
    private EditText workoutNameEditText;
    private EditText workoutDurationEditText;
    private EditText workoutCaloriesEditText;
    private Workout workout; // The workout object passed from ActivityWorkout

    private DatabaseReference databaseReference; // Reference to the database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete); // Ensure this matches your layout

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("workouts");

        // Initialize EditTexts
        workoutNameEditText = findViewById(R.id.workoutNameEditText);
        workoutDurationEditText = findViewById(R.id.workoutDurationEditText);
        workoutCaloriesEditText = findViewById(R.id.workoutCaloriesEditText);

        // Initialize buttons and set onClick listeners
        findViewById(R.id.updateButton).setOnClickListener(this::updateWorkout);
        findViewById(R.id.deleteButton).setOnClickListener(this::deleteWorkout);

        // Get the workout data passed from ActivityWorkout
        Intent intent = getIntent();
        String workoutId = intent.getStringExtra("WORKOUT_ID");
        String workoutName = intent.getStringExtra("WORKOUT_NAME");
        int workoutDuration = intent.getIntExtra("WORKOUT_DURATION", 0);
        int workoutCalories = intent.getIntExtra("WORKOUT_CALORIES", 0);

        // Create Workout object
        workout = new Workout(workoutId, workoutName, workoutDuration, workoutCalories);

        // Populate fields with workout data
        populateFields();
    }


    // Method to populate EditText fields
    private void populateFields() {
        workoutNameEditText.setText(workout.getName());
        workoutDurationEditText.setText(String.valueOf(workout.getDuration()));
        workoutCaloriesEditText.setText(String.valueOf(workout.getCalories()));
    }

    // Method to update the workout
    public void updateWorkout(View view) {
        Log.d("ActivityUpdateDelete", "Update button clicked");

        // Get updated data from EditTexts
        String updatedName = workoutNameEditText.getText().toString().trim();
        String updatedDurationStr = workoutDurationEditText.getText().toString().trim();
        String updatedCaloriesStr = workoutCaloriesEditText.getText().toString().trim();

        // Validate inputs
        if (validateInputs(updatedName, updatedDurationStr, updatedCaloriesStr)) {
            // Parse updated values
            int updatedDuration = Integer.parseInt(updatedDurationStr);
            int updatedCalories = Integer.parseInt(updatedCaloriesStr);

            // Create updated Workout object
            Workout updatedWorkout = new Workout(workout.getId(), updatedName, updatedDuration, updatedCalories);

            // Update workout data in Firebase
            databaseReference.child(workout.getId()).setValue(updatedWorkout)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ActivityUpdateDelete", "Update successful");
                            returnToActivity(updatedWorkout);
                        } else {
                            Log.e("ActivityUpdateDelete", "Update failed", task.getException());
                            showToast("Update failed");
                        }
                    });
        }
    }

    // Method to validate input fields
    private boolean validateInputs(String name, String duration, String calories) {
        if (name.isEmpty() || duration.isEmpty() || calories.isEmpty()) {
            showToast("Please fill all fields");
            return false;
        }
        try {
            Integer.parseInt(duration);
            Integer.parseInt(calories);
        } catch (NumberFormatException e) {
            showToast("Please enter valid numbers for duration and calories");
            return false;
        }
        return true;
    }

    // Method to return updated workout data to the previous activity
    private void returnToActivity(Workout updatedWorkout) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_WORKOUT", updatedWorkout);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Optional: Method to delete the workout
    public void deleteWorkout(View view) {
        Log.d("ActivityUpdateDelete", "Delete button clicked");

        // Delete workout data from Firebase
        databaseReference.child(workout.getId()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ActivityUpdateDelete", "Deletion successful");
                        returnDeletedWorkoutId();
                    } else {
                        Log.e("ActivityUpdateDelete", "Deletion failed", task.getException());
                        showToast("Deletion failed");
                    }
                });
    }

    // Method to return deleted workout ID to the previous activity
    private void returnDeletedWorkoutId() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("DELETED_WORKOUT_ID", workout.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Helper method to show Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
