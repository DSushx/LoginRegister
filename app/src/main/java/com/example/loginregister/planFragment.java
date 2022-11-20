package com.example.loginregister;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;


public class planFragment extends Fragment {
    Button buttonSave,t;
    TextInputEditText plan_Weightnow;
    TextInputEditText plan_Weight;
    RadioGroup plan_radioGroup;
    RadioGroup plan_radioGroup2;
    RadioButton plan_selectedRadioButton;
    RadioButton plan_selectedRadioButton2;
    private EditText plan_applydate = null;
    private int plan_mYear, plan_mMonth, plan_mDay;
    TextInputEditText plan_Date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_plan, container, false);
        buttonSave =view.findViewById(R.id.buttonsave1);
        plan_Weightnow =view.findViewById(R.id.plan_weightnow1);
        plan_Weight =view.findViewById(R.id.plan_weight1);
        plan_Date = view.findViewById(R.id.plan_date1);
        t=(Button)view.findViewById(R.id.buttonNU1);
        plan_radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup11);
        plan_radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup21);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondfrag = new NuFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,secondfrag).commit();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan_weightnow,plan_weight,startdate,special_dis,blood,name1;
                plan_weightnow = String.valueOf(plan_Weightnow.getText());  //取得輸入的資料
                plan_weight = String.valueOf(plan_Weight.getText());

                startdate = String.valueOf(plan_Date.getText());
                plan_selectedRadioButton  = (RadioButton)view.findViewById(plan_radioGroup.getCheckedRadioButtonId());
                plan_selectedRadioButton2  = (RadioButton)view.findViewById(plan_radioGroup2.getCheckedRadioButtonId());
                special_dis = plan_selectedRadioButton.getText().toString();
                blood = plan_selectedRadioButton2.getText().toString();
                StringBuffer result = new StringBuffer();

                //Toast.makeText(getActivity(),strtext,Toast.LENGTH_SHORT).show();
                if(!plan_weightnow.equals("")&&!plan_weight.equals("")&&!startdate.equals("")&&!special_dis.equals("")&&!blood.equals("")) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable(){
                        @Override
                        public void run() {

                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[5];
                            field[0] = "plan_weightnow";
                            field[1] = "plan_weight";
                            field[2] = "startdate";
                            field[3] = "special_dis";
                            field[4] = "blood";

                            //Creating array for data
                            String[] data = new String[5];
                            data[0] = plan_weightnow;
                            data[1] = plan_weight;
                            data[2] = startdate;
                            data[3] = special_dis;
                            data[4] = blood;

                            PutData putData = new PutData("http://10.0.2.2/LoginRegister/plan.php", "POST", field, data);

                            Toast.makeText(getActivity(),result.toString(),Toast.LENGTH_LONG).show();
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(),planFragment.class);
                                        startActivity(intent);


                                    }
                                    else{
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();

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