package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tripsFragment extends Fragment {
    private static String tripName, cityName, dates;
    private static TextView tripNameField, cityNameField, cityDatesField;
    private static int tripId;
    private static RecyclerView tripDaysRecycle;
    private static ScrollView scroll;
    private static ProgressBar progressBar;
    private static boolean dontUpdateData;

    public tripsFragment() {
    }

    public tripsFragment(String tripName, String cityName, String dates, int tripId)
    {
        this.tripName = tripName;
        this.cityName = cityName;
        this.dates = dates;
        this.tripId = tripId;
    }

    public tripsFragment(Context context, ServerRequester.TripInfo tripInfo)
    {
        dontUpdateData = true;
        UpdateEntities(context, tripInfo);
    }

    // TODO: Rename and change types and number of parameters
    public static tripsFragment newInstance() {
        tripsFragment fragment = new tripsFragment();
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
        View v = inflater.inflate(R.layout.fragment_trips, container, false);

        scroll = v.findViewById(R.id.trip_scroll);
        scroll.setVisibility(View.INVISIBLE);

        progressBar = v.findViewById(R.id.trips_progessBar);

        tripNameField = v.findViewById(R.id.tripName_field);
        cityNameField = v.findViewById(R.id.tripCityName_field);
        cityDatesField = v.findViewById(R.id.tripDates_field);

        tripDaysRecycle = v.findViewById(R.id.tripDays_recycle);

        if (tripName != null && tripName != "")
            tripNameField.setText(tripName);
        if (cityName != null && cityName != "")
            cityNameField.setText(cityName);
        if (dates != null && dates != "")
            cityDatesField.setText(dates);

        //if(!dontUpdateData)
            ServerRequester.GetTripInfo(v.getContext(), tripId);
        dontUpdateData = false;


        return v;
    }

    public static void UpdateEntities(Context context, ServerRequester.TripInfo data)
    {
        tripName = (data.Name);
        tripNameField.setText(data.Name);

        cityName = data.DestinationName;
        cityNameField.setText(data.DestinationName);

        dates = data.TripStart + " - " + data.TripEnd;
        cityDatesField.setText(dates);

        LinearLayoutManager verticalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }};
        tripDaysRecycle.setLayoutManager(verticalManager);
        TripDaysRecyclerAdapter adapter = new TripDaysRecyclerAdapter(context, data.TripDays);
        tripDaysRecycle.setAdapter(adapter);

        scroll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void ShowNoTripsFragment(Context context)
    {
        ((AppCompatActivity)context).onBackPressed();
        Helpers.loadFragment(new NoTripsFragment(), ((AppCompatActivity)context).getSupportFragmentManager());

    }
}