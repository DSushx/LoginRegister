package com.example.loginregister.plan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginregister.PlanFragment;
import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.NowPlanInfo;
import com.example.loginregister.datasets.PlanInfo;
import com.example.loginregister.datasets.UserInfo;
import com.example.loginregister.home.SharedViewModel;
import com.example.loginregister.suggestion.CustomList;

import java.util.List;


public class pastplanFragment extends Fragment {
    public pastplanFragment() {
        super(R.layout.nowplan);
    }
    Context mContext;
    Button back;
    ProgressBar progressBar;
    TextView result,nowplandate,realnp_weight,realnp_weightnow;
    List<PlanInfo> planData;
    NowPlanInfo nowplanData;
    planlist planlist;
    ListView pastplan_show,show;
    SharedViewModel viewModel;
    View mView;
    String startdate;
    Double weight,weightnow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pastplan, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);
        back =view.findViewById(R.id.pastplanback);
        progressBar=view.findViewById(R.id.progressBarpastplan);
        result=view.findViewById(R.id.pastplan_no_result);
        mView = view;
        nowplandate=view.findViewById(R.id.realnowplandate);
        realnp_weight=view.findViewById(R.id.realnp_weight);
        realnp_weightnow=view.findViewById(R.id.realnp_weightnow);
        pastplan_show=view.findViewById(R.id.pastplan_show);
        progressBar.setVisibility(View.VISIBLE);
        mView = view;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondfrag = new PlanFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,secondfrag).commit();
            }
        });
//        new Thread(() -> {
//            nowplanData = viewModel.getCon().getnowplanData(uname);
//            Log.i("Nowplan", nowplanData.toString());
//            startdate=nowplanData.nowstartdate;
//            weight= nowplanData.nowplan_weight;
//            weightnow=nowplanData.nowplan_weightnow;
//            int i = Integer.valueOf(weight.intValue());
//            int j = Integer.valueOf(weightnow.intValue());
//            TextView realstartdate = view.findViewById(R.id.realnowplandate);
//            TextView realweightnow = view.findViewById(R.id.realnp_weightnow);
//            TextView realweightplan = view.findViewById(R.id.realnp_weight);
//            realstartdate.setText(startdate);
//            realweightnow.setText(i);
//            realweightplan.setText(j);
//
//
//        }).start();

        refreshList(view);


        return view;
    }

    public void refreshList(View v) {

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