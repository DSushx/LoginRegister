package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

import java.sql.Date;

public class UserInfo {
    public Integer user_id;
    public String fullname;
    public String username;
    public String password;
    public String email;
    public Integer height;
    public Integer weight;
    public Date birthday;
    public String gender;

    @NonNull
    public String toString(){
        return "[" + user_id + "," + fullname + "," + username + "," + password + "," +
                email + "," + height + "," + weight + "," + birthday + "," + gender + "]";
    }
}
