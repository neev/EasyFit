package com.example.android.easyfitness.data;

/**
 * Created by neeraja on 3/11/2016.
 */
public class WorkoutRecord {
    int workoutDuration;
    String workoutDesc;

    public int getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutDuration(int workoutDuration) {
        this.workoutDuration = workoutDuration;
    }

    public String getWorkoutDesc() {
        return workoutDesc;
    }

    public void setWorkoutDesc(String workoutDesc) {
        this.workoutDesc = workoutDesc;
    }

    public WorkoutRecord() {
    }

    public WorkoutRecord(int workoutDuration, String workoutDesc) {
        this.workoutDuration = workoutDuration;
        this.workoutDesc = workoutDesc;
    }
}
