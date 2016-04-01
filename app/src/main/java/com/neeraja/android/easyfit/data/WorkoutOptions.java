package com.neeraja.android.easyfit.data;

/**
 * Created by neeraja on 3/6/2016.
 */
public class WorkoutOptions {
    private int id;
    private String workout;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }
    public WorkoutOptions(){

    }
    public WorkoutOptions(int id, String workout) {
        this.id = id;
        this.workout = workout;
    }
}
