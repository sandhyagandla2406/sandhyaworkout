package com.canadore.sandhyaworkout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {

    private Context context;
    private List<Workout> workoutList;

    public WorkoutAdapter(Context context, List<Workout> workoutList) {
        super(context, 0, workoutList);
        this.context = context;
        this.workoutList = workoutList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Workout workout = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_workout, parent, false);
        }

        TextView workoutNameTextView = convertView.findViewById(R.id.workoutNameTextView);
        TextView workoutDurationTextView = convertView.findViewById(R.id.workoutDurationTextView);
        TextView workoutCaloriesTextView = convertView.findViewById(R.id.workoutCaloriesTextView);

        // Populate the views with workout data
        workoutNameTextView.setText(workout.getName());
        workoutDurationTextView.setText("Duration: " + workout.getDuration() + " mins");
        workoutCaloriesTextView.setText("Calories: " + workout.getCalories() + " cals");

        // Set a click listener to launch the ActivityUpdateDelete when an item is clicked
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityUpdateDelete.class);
            intent.putExtra("WORKOUT_ID", workout.getId()); // Ensure this method exists
            context.startActivity(intent);
        });

        return convertView;
    }
}
