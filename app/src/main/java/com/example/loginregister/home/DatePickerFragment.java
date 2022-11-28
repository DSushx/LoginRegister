package com.example.loginregister.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.loginregister.R;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    //TextView textdate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        //textdate=rootView.findViewById(R.id.TextDate);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @SuppressLint("RestrictedApi")
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        Log.d(TAG,"onDataSet : yyyy/mm/dd: " + year + "/"+month+"/"+day );
        String date = year + "/"+month+"/"+day;
        TextView textdate = getActivity().findViewById(R.id.TextDate);
        textdate.setText(date);


    }
}
