package com.example.loginregister;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;


public class NuFragment extends Fragment {
    Button nusave,back;
    TextInputEditText nu_kcal,nu_pro,nu_fat,nu_car,nu_sugar,nu_sod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_nu, container, false);
        nu_kcal =view.findViewById(R.id.NU_ca1);
        nu_pro =view.findViewById(R.id.NU_pro1);
        nu_fat =view.findViewById(R.id.NU_fat1);
        nu_car =view.findViewById(R.id.NU_car1);
        nu_sugar =view.findViewById(R.id.NU_sugar1);
        nu_sod =view.findViewById(R.id.NU_sod1);

        nusave =view.findViewById(R.id.nusave);
        back =view.findViewById(R.id.nubacktoplan);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondfrag = new PlanFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,secondfrag).commit();
            }
        });
        nusave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calorie,protein,fat,carbohy,sugar,sodium,uname;
                calorie = String.valueOf(nu_kcal.getText());  //取得輸入的資料
                protein = String.valueOf(nu_pro.getText());
                fat = String.valueOf(nu_fat.getText());  //取得輸入的資料
                carbohy = String.valueOf(nu_car.getText());
                sugar = String.valueOf(nu_sugar.getText());  //取得輸入的資料
                sodium= String.valueOf(nu_sod.getText());

                StringBuffer result = new StringBuffer();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                uname = pref.getString("username", null);
                //Toast.makeText(getActivity(),strtext,Toast.LENGTH_SHORT).show();
                if(!calorie.equals("")&&!protein.equals("")&&!fat.equals("")&&!carbohy.equals("")&&!sugar.equals("")&&!sodium.equals("")&&!uname.equals("")) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable(){
                        @Override
                        public void run() {

                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "calorie";
                            field[1] = "protein";
                            field[2] = "fat";
                            field[3] = "carbohy";
                            field[4] = "sugar";
                            field[5] = "sodium";
                            field[6] = "uname";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = calorie;
                            data[1] = protein;
                            data[2] = fat;
                            data[3] = carbohy;
                            data[4] = sugar;
                            data[5] = sodium;
                            data[6] = uname;
                            PutData putData = new PutData("http://192.168.1.72/LoginRegister/nu.php", "POST", field, data);

                            Toast.makeText(getActivity(),result.toString(),Toast.LENGTH_LONG).show();
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    String result = putData.getResult();
                                    if (result.equals("Success")){
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(getActivity(),NuFragment.class);
                                        //startActivity(intent);


                                    }
                                    else{
                                        //Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();

                                    }
                                    Log.i("PutData", result);
                                }
                            }
                            //End Write and Read data with URL
                        }


                    });
                }
                else {

                    Toast.makeText(getActivity(),"All fields required",Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
}