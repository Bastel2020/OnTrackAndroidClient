package com.bastel2020.ontrack;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Helpers {

    public static View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((AppCompatActivity) v.getContext()).onBackPressed();
        }
    };

    public static void loadFragment(Fragment fragment, FragmentManager fm) {
        // load fragment
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.default_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
