package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class GoalActiveLevelNu {
    public int Goal = 1;
    public int ActiveLevel = 0;

    public NuInfo NuInfo;

    @NonNull
    public String toString(){
        return "[" + Goal + "," + ActiveLevel + "," + NuInfo.toString() +  "]";
    }
}
