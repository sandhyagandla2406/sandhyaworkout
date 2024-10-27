package com.canadore.sandhyaworkout;

import java.io.Serializable;

public class Workout implements Serializable {
    private String id;
    private String name;
    private int duration; // in minutes
    private int calories; // burned

    public Workout(String id, String name, int duration, int calories) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.calories = calories;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return name; // This will show in the ListView
    }
}
