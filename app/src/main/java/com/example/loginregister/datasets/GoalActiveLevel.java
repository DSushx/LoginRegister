package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class GoalActiveLevel {
    public int Goal = 1;
    public int ActiveLevel = 0;

    @NonNull
    public String toString(){
        return "[" + Goal + "," + ActiveLevel +  "]";
    }
}
