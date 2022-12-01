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
    TextView result;
    List<PlanInfo> planData;
    planlist planlist;
    ListView pastplan_show;
    SharedViewModel viewModel;
    View mView;
    UserInfo userData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pastplan, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        back =view.findViewById(R.id.pastplanback);
        progressBar=view.findViewById(R.id.progressBarpastplan);
        result=view.findViewById(R.id.pastplan_no_result);
        pastplan_show=view.findViewById(R.id.pastplan_show);
        progressBar.setVisibility(View.VISIBLE);
        mView = view;
        refreshList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondfrag = new PlanFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,secondfrag).commit();
            }
        });
        return view;
    }
    public void refreshList() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uname = pref.getString("username", null);
        result.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {

            planData = viewModel.getCon().getPlanData(uname);
            Log.v("OK", "食物資料已回傳");
            Log.i("foodData", planData.toString());

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