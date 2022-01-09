package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link activeTripsInProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class activeTripsInProfileFragment extends Fragment {


    public activeTripsInProfileFragment() { }

    public activeTripsInProfileFragment(Context context, ServerRequester.TripShortInfo[] data)
    {
        AppCompatActivity v = (AppCompatActivity) context;
        if(data == null || data.length == 0)
            return;

        TextView noTripsLabel = v.findViewById(R.id.noActiveTrips_label);
        noTripsLabel.setVisibility(View.INVISIBLE);

        RecyclerView activeTripsRecycler = v.findViewById(R.id.activeTrips_recycle);

        LinearLayoutManager verticalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        activeTripsRecycler.setLayoutManager(verticalManager);

        activeTripsRecycler.setAdapter(new TripsInProfileRecyclerAdapter(context, data));
    }

    public static activeTripsInProfileFragment newInstance() {
        activeTripsInProfileFragment fragment = new activeTripsInProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_active_trips_in_profile, container, false);

        return v;
    }
}