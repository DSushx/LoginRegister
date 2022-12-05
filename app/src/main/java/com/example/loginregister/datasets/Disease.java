package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class Disease
{
    public String dis,nu;
    public String weight;
    @NonNull
    public String toString(){
        return "[" + dis + "," + nu +  "]";
    }
}
