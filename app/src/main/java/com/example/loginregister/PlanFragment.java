package com.example.loginregister;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.loginregister.datasets.Disease;
import com.example.loginregister.datasets.GoalActiveLevelNu;
import com.example.loginregister.home.SharedViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;


public class PlanFragment extends Fragment {
    SharedViewModel viewModel;
    Button buttonSave,t;
    TextInputEditText plan_Weightnow;
    TextInputEditText plan_Weight;
    RadioGroup plan_radioGroup;
    RadioGroup plan_radioGroup2;
    RadioGroup plan_radioGroup3;
    RadioButton plan_selectedRadioButton;
    RadioButton plan_selectedRadioButton2;
    RadioButton plan_selectedRadioButton3;

    ViewGroup c ;
    private EditText plan_applydate = null;
    private int plan_mYear, plan_mMonth, plan_mDay;
    TextInputEditText plan_Date;
    private void cleanEditText() {
        plan_Weightnow.setText("");
        plan_Weight.setText("");
        plan_Date.setText("");
        plan_radioGroup.check(R.id.rb111);
        plan_radioGroup2.check(R.id.rbnu11);
        plan_radioGroup3.check(R.id.planrb11);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_plan, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        buttonSave =view.findViewById(R.id.buttonsave1);
        plan_Weightnow =view.findViewById(R.id.plan_weightnow1);
        plan_Weight =view.findViewById(R.id.plan_weight1);
        plan_Date = view.findViewById(R.id.plan_date1);
        t=(Button)view.findViewById(R.id.buttonNU1);
        plan_radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup11);
        plan_radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup21);
        plan_radioGroup3 = (RadioGroup) view.findViewById(R.id.exragr);

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
                String plan_weightnow,plan_weight,startdate,special_dis,nutrient,exercise,uname;
                plan_weightnow = String.valueOf(plan_Weightnow.getText());  //取得輸入的資料
                plan_weight = String.valueOf(plan_Weight.getText());

                startdate = String.valueOf(plan_Date.getText());
                plan_selectedRadioButton  = (RadioButton)view.findViewById(plan_radioGroup.getCheckedRadioButtonId());
                plan_selectedRadioButton2  = (RadioButton)view.findViewById(plan_radioGroup2.getCheckedRadioButtonId());
                plan_selectedRadioButton3 = (RadioButton)view.findViewById(plan_radioGroup3.getCheckedRadioButtonId());
                special_dis = plan_selectedRadioButton.getText().toString();
                nutrient = plan_selectedRadioButton2.getText().toString();
                exercise = plan_selectedRadioButton3.getText().toString();
                StringBuffer result = new StringBuffer();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                 uname = pref.getString("username", null);
                //Toast.makeText(getActivity(),strtext,Toast.LENGTH_SHORT).show();
                if(!plan_weightnow.equals("")&&!plan_weight.equals("")&&!startdate.equals("")&&!special_dis.equals("")&&!nutrient.equals("")&&!exercise.equals("")&&!uname.equals("")) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable(){
                        @Override
                        public void run() {

                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "plan_weightnow";
                            field[1] = "plan_weight";
                            field[2] = "startdate";
                            field[3] = "special_dis";
                            field[4] = "nutrient";
                            field[5] = "exercise";
                            field[6] = "uname";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = plan_weightnow;
                            data[1] = plan_weight;
                            data[2] = startdate;
                            data[3] = special_dis;
                            data[4] = nutrient;
                            data[5] = exercise;
                            data[6] = uname;
                            PutData putData = new PutData("http://192.168.0.155/LoginRegister/plan.php", "POST", field, data);

                            Toast.makeText(getActivity(),result.toString(),Toast.LENGTH_LONG).show();
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    String result = putData.getResult();
                                    if (result.equals("Plan Success")){
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();

                                        int goal,activeLevel;
                                        if (Double.parseDouble(plan_weight)-Double.parseDouble(plan_weightnow)>0){
                                            goal=2;
                                        }
                                        else if (Double.parseDouble(plan_weight)-Double.parseDouble(plan_weightnow)==0){
                                            goal=1;
                                        }
                                        else   {goal=0;}
                                        if (exercise.equals("高度")){
                                            activeLevel=2;
                                        }
                                        else if (exercise.equals("中度")){
                                            activeLevel=1;
                                        }
                                        else   {activeLevel=0;}

                                        viewModel.setGoalActiveLevel(goal, activeLevel);

                                        Disease diseaseandnu  = new Disease();
                                        diseaseandnu.dis = special_dis;
                                        diseaseandnu.nu = nutrient;
                                        diseaseandnu.weight = plan_weightnow;

                                        viewModel.setdiseaseandnu(diseaseandnu);

                                        Fragment secondfrag = new PlanFragment();
                                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                        fm.replace(R.id.container,secondfrag).commit();


                                    }
                                    else{
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();

                                    }
                                    Log.i("PutData", result);
                                    cleanEditText();
                                }
                            }
                            //End Write and Read data with URL
                        }


                    });
                }
                else {

                    Toast.makeText(getActivity(),"All fields required",Toast.LENGTH_SHORT).show();
                    cleanEditText();
                }
            }
        });
        return view;
    }

}