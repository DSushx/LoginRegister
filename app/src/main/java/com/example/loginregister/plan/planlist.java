package com.example.loginregister.plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.loginregister.R;
import com.example.loginregister.datasets.PlanInfo;

import java.util.List;

public class planlist extends BaseAdapter {
    private final Context mContext;
    private final pastplanFragment pa;
    private final List<PlanInfo> planInfo;
    public planlist(Context mContext, pastplanFragment pa, List<PlanInfo> planInfo) {
        this.mContext = mContext;
        this.pa = pa;
        this.planInfo = planInfo;
    }

    @Override
    public int getCount() {
        return planInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        View list;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            list = inflater.inflate(R.layout.nowplan, container, false);

        } else {

            list = convertView;
        }
        TextView startdate = list.findViewById(R.id.nowplandate);
        TextView enddate = list.findViewById(R.id.nowplandate2);
        TextView weightnow = list.findViewById(R.id.np_weightnow);
        TextView weightplan = list.findViewById(R.id.np_weight);
        TextView finalweight = list.findViewById(R.id.np_finalweight);
        TextView timebar = list.findViewById(R.id.nowplandate1_5);
        TextView plan_name = list.findViewById(R.id.plan_name);
        TextView ratio = list.findViewById(R.id.plan_ratio);



        timebar.setText("~");
        String Str="%";
        Double t =((planInfo.get(position).final_weight -planInfo.get(position).plan_weightnow)/(planInfo.get(position).plan_weight -planInfo.get(position).plan_weightnow))*100;
        int i =  Integer.valueOf(t.intValue());
        ratio.setText(String.format("%s %s", i,Str));

        startdate.setText(planInfo.get(position).startdate);
        enddate.setText(planInfo.get(position).enddate);
        weightnow.setText(String.format("%s kg", planInfo.get(position).plan_weightnow));
        weightplan.setText(String.format("%s kg", planInfo.get(position).plan_weight));
        finalweight.setText(String.format("%s kg", planInfo.get(position).final_weight));

        return list;
    }
}
