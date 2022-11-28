package com.example.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.datasets.UserInfo;
import com.example.loginregister.home.SharedViewModel;
import com.example.loginregister.suggestion.MysqlCon;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    SharedViewModel viewModel;
    UserInfo userData;
    MysqlCon con;
    String ubirthday,disease,nutrient;
    Integer uheight,uweight;
    TextView name,birthday,height,weight,dis,nu;
    Button logout;
    //ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        logout=(Button)view.findViewById(R.id.buttonlogout);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        new Thread(() -> {
            userData = viewModel.getCon().getUserData(uname);
            Log.i("userData", userData.toString());
            disease=userData.disease;
            nutrient=userData.nutrient;
            ubirthday= String.valueOf(userData.birthday);
            uheight=userData.height;
            uweight=userData.weight;
            String h = String.valueOf(uheight);
            String w = String.valueOf(uweight);
            name =view.findViewById(R.id.editTextTextPersonName);
            birthday =view.findViewById(R.id.editTextTextPersonName3);
            height =view.findViewById(R.id.editTextTextPersonName5);
            weight = view.findViewById(R.id.editTextTextPersonName4);
            dis =view.findViewById(R.id.editTextTextPersonName8);
            nu = view.findViewById(R.id.editTextTextPersonName9);

            name.setText(uname);
            birthday.setText(ubirthday);
            height.setText(h);
            weight.setText(w);
            dis.setText(disease);
            nu.setText(nutrient);
        }).start();



        return view;
    }
}