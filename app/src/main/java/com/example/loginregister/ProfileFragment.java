package com.example.loginregister;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.datasets.UserInfo;
import com.example.loginregister.suggestion.MysqlCon;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextView name,birthday,height,weight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);
        //Integer h = pref.getInt("hei", 0);
        name =view.findViewById(R.id.editTextTextPersonName);
        birthday =view.findViewById(R.id.textView5);
        height =view.findViewById(R.id.textView6);
        weight = view.findViewById(R.id.textView7);


        name.setText(uname);
        return view;
    }
}