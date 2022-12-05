package com.example.loginregister.plan;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.PlanFragment;
import com.example.loginregister.R;
import com.example.loginregister.datasets.Disease;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.GoalActiveLevelNu;
import com.example.loginregister.datasets.NowPlanInfo;
import com.example.loginregister.datasets.PlanInfo;
import com.example.loginregister.datasets.UserInfo;
import com.example.loginregister.home.HomeList;
import com.example.loginregister.home.SharedViewModel;
import com.example.loginregister.suggestion.CustomList;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;


public class pastplanFragment extends Fragment {

    Context mContext;
    Button back,save;
    TextInputEditText nowplan_finalWeight;
    ProgressBar progressBar;
    TextView result,nowplandate,realnp_weight,realnp_weightnow,realstartdate,realweightnow,realweightplan,date1,weightplan,upresult;
    TextView ini_w,ini_w2,ini_w3,planname;
    ImageView im1,im2,im3,planph;
    List<PlanInfo> planData;
    NowPlanInfo nowplanData;
    planlist planlist;
    ListView pastplan_show,show;
    SharedViewModel viewModel;
    String startdate,exercise;
    Double weight,weightnow;
    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pastplan, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);
        ini_w =view.findViewById(R.id.nowplanText1);
        ini_w2 =view.findViewById(R.id.nowplanText2);
        ini_w3 =view.findViewById(R.id.nowplanText3);
        im1 =view.findViewById(R.id.nowplanima1);
        im2 =view.findViewById(R.id.nowplanima2);
        im3 =view.findViewById(R.id.nowplanima3);
        planname=view.findViewById(R.id.plan_name1);
        planph=view.findViewById(R.id.plan_photo1);
        progressBar=view.findViewById(R.id.progressBarpastplan);
        upresult=view.findViewById(R.id.nowplan_no_result);
        result=view.findViewById(R.id.pastplan_no_result);
        nowplan_finalWeight=view.findViewById(R.id.realnp_finalweight);
        nowplandate=view.findViewById(R.id.realnowplandate);
        realnp_weight=view.findViewById(R.id.realnp_weight);
        realnp_weightnow=view.findViewById(R.id.realnp_weightnow);
        back=(Button)view.findViewById(R.id.pastplanback);
        save=(Button)view.findViewById(R.id.nowplansave);
        pastplan_show=view.findViewById(R.id.pastplan_show);
        progressBar.setVisibility(View.VISIBLE);
        mView=view;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondfrag = new PlanFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,secondfrag).commit();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String final_weight,end_date,uname,startdate,plan_weight;
                end_date="test";
                final_weight= String.valueOf(nowplan_finalWeight.getText());
                plan_weight=String.valueOf(weightplan.getText());
                startdate=String.valueOf(date1.getText());
                StringBuffer result = new StringBuffer();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                uname = pref.getString("username", null);

                if(!final_weight.equals("")&&!end_date.equals("")&&!uname.equals("")) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable(){
                        @Override
                        public void run() {

                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[5];
                            field[0] = "plan_weight";
                            field[1] = "startdate";
                            field[2] = "uname";
                            field[3] = "end_date";
                            field[4] = "final_weight";

                            //Creating array for data
                            String[] data = new String[5];
                            data[0] = plan_weight;
                            data[1] = startdate;
                            data[2] = uname;
                            data[3] = end_date;
                            data[4] = final_weight;

                            PutData putData = new PutData("http://192.168.1.72/LoginRegister/nowplan.php", "POST", field, data);

                            Toast.makeText(getActivity(),result.toString(),Toast.LENGTH_LONG).show();
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    String result = putData.getResult();
                                    if (result.equals("Success!")){
                                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(getActivity(),planFragment.class);
                                        //startActivity(intent);

                                        Disease diseaseandnu  = new Disease();
                                        diseaseandnu.dis = " ";
                                        diseaseandnu.nu = " ";
                                        diseaseandnu.weight = final_weight;

                                        viewModel.setdiseaseandnu(diseaseandnu);
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

                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment nextfrag = new pastplanFragment();
                fm.replace(R.id.container,nextfrag).commit();
            }
        });
        new Thread(() -> {
            nowplanData = viewModel.getCon().getnowplanData(uname);
            Log.i("Nowplan", nowplanData.toString());
            startdate=nowplanData.nowstartdate;
            weight= nowplanData.nowplan_weight;
            weightnow=nowplanData.nowplan_weightnow;
            exercise=nowplanData.exercise;
            String i = String.valueOf(weight.intValue());
            String j = String.valueOf(weightnow.intValue());
            realstartdate = view.findViewById(R.id.realnowplandate);
            realweightnow = view.findViewById(R.id.realnp_weightnow);
            realweightplan = view.findViewById(R.id.realnp_weight);

            view.post(() -> {
                if (weight==0) {
                    upresult.setVisibility(View.VISIBLE);

                }

                else {
                    ini_w.setVisibility(View.VISIBLE);
                    ini_w2.setVisibility(View.VISIBLE);
                    ini_w3.setVisibility(View.VISIBLE);
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    nowplan_finalWeight.setVisibility(View.VISIBLE);
                    planname.setVisibility(View.VISIBLE);
                    planph.setVisibility(View.VISIBLE);
                    realstartdate.setText(startdate);
                    realweightnow.setText(j);
                    realweightplan.setText(i);
                    date1 = view.findViewById(R.id.realnowplandate);
                    weightplan = view.findViewById(R.id.realnp_weight);
                }
            });



        }).start();

        refreshList();

        return view;
    }

    public void refreshList() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);

        result.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {

            planData = viewModel.getCon().getPlanData(uname);
            Log.v("OK", "計畫已回傳");
            Log.i("PlanData", planData.toString());
            //Log.i("NowData",planData.toString());
            mView.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                planlist = new planlist(requireContext(), this, planData);
                pastplan_show.setAdapter(planlist);
                if (planData.isEmpty()) {
                    result.setVisibility(View.VISIBLE);
                }


            });

        }).start();
    }
}