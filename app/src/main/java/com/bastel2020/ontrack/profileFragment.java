package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    private static TextView name, email;
    private static ScrollView scroll;
    private static ProgressBar progressBar;
    private static Fragment activeTripsFragment;
    private static ImageButton logOut;

    public profileFragment() {
        // Required empty public constructor
    }

    public static profileFragment newInstance() {
        profileFragment fragment = new profileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        scroll = v.findViewById(R.id.profile_scroll);
        scroll.setVisibility(View.INVISIBLE);

        progressBar = v.findViewById(R.id.profile_progressBar);

        name = v.findViewById(R.id.nameInProfile_field);
        email = v.findViewById(R.id.emailInProfile_field);
        activeTripsFragment = new activeTripsInProfileFragment();
        logOut = v.findViewById(R.id.logOut_button);

        ViewStateAdapter sa = new ViewStateAdapter(getChildFragmentManager(), getLifecycle());
        final ViewPager2 pa = v.findViewById(R.id.profile_pagger);
        pa.setAdapter(sa);

        TabLayout tabLayout = v.findViewById(R.id.tripInProfile_tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Активные"));
//        tabLayout.addTab(tabLayout.newTab().setText("Архив"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pa.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pa.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        ServerRequester.LoadProfileInfo(getContext());

        return v;
    }

    public static void UpdateEntities(Context context, ServerRequester.UserInfo data)
    {
        name.setText(data.Username);
        email.setText(data.Email);
        activeTripsFragment = new activeTripsInProfileFragment(context, data.UserTrips);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Выход из системы...", Toast.LENGTH_SHORT).show();

                DbContext.DeleteToken();

                ServerRequester.IsValidToken(v.getContext());

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fm = activity.getSupportFragmentManager();
                int count = fm.getBackStackEntryCount();
                for(int i = 0; i < count; i++)
                {
                    fm.popBackStack();
                }
                Helpers.loadFragment(new MainFragment(), activity.getSupportFragmentManager());
            }
        });

        scroll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class ViewStateAdapter extends FragmentStateAdapter {

        public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Hardcoded in this order, you'll want to use lists and make sure the titles match
            if (position == 0) {
                return activeTripsFragment;
            }
            return new archiveTripsInProfileFragment();
        }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return 2;
        }
    }
}