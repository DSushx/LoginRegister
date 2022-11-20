package com.example.loginregister.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginregister.InsertFragment;
import com.example.loginregister.ProfileFragment;
import com.example.loginregister.planFragment;
import com.example.loginregister.suggestion.SuggestionFragment;

//import com.example.loginregister.profile2.userprofilefragment;

public class HomePager extends FragmentStateAdapter {
    public HomePager(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch(position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new InsertFragment();
                break;
            case 2:
                fragment = new SuggestionFragment();
                break;
            case 3:
                fragment = new ProfileFragment();
                break;
            case 4:
                fragment = new planFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        return fragment;
    }
}
