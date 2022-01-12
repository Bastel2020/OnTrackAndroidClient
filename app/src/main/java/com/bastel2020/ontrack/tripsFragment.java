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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tripsFragment extends Fragment {
    private static String tripName, cityName, dates;
    private static TextView tripNameField, cityNameField, cityDatesField, memberChar1, memberChar2, memberChar3;
    private static CircleImageView member1, member2, member3;
    private static int tripId;
    private static RecyclerView tripDaysRecycle;
    private static ScrollView scroll;
    private static ProgressBar progressBar;
    private static boolean dontUpdateData;
    private static ServerRequester.TripInfo loadedTrip;

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
        loadedTrip = tripInfo;
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

        memberChar1 = v.findViewById(R.id.MemberChar1);
        memberChar1.setVisibility(View.INVISIBLE);
        memberChar2 = v.findViewById(R.id.MemberChar2);
        memberChar2.setVisibility(View.INVISIBLE);
        memberChar3 = v.findViewById(R.id.MemberChar3);
        memberChar3.setVisibility(View.INVISIBLE);

        member1 = v.findViewById(R.id.MemberCircle1);
        member1.setVisibility(View.INVISIBLE);
        member2 = v.findViewById(R.id.MemberCircle2);
        member2.setVisibility(View.INVISIBLE);
        member3 = v.findViewById(R.id.MemberCircle3);
        member3.setVisibility(View.INVISIBLE);

        if(dontUpdateData)
            UpdateEntities(getContext(), loadedTrip);
        else
            ServerRequester.GetTripInfo(v.getContext(), tripId, false);

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
                return true;
            }};
        tripDaysRecycle.setLayoutManager(verticalManager);
        TripDaysRecyclerAdapter adapter = new TripDaysRecyclerAdapter(context, data.TripDays);
        tripDaysRecycle.setAdapter(adapter);

        View.OnClickListener goToUsers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loadFragment(new TripMembersFragment(data.Id), ((AppCompatActivity)v.getContext()).getSupportFragmentManager());
            }
        };

        if(data.users != null && data.users.length > 0) {
            member3.setVisibility(View.VISIBLE);
            memberChar3.setVisibility(View.VISIBLE);

            member3.setOnClickListener(goToUsers);

            if (data.users.length > 3) {
                member1.setVisibility(View.VISIBLE);
                member2.setVisibility(View.VISIBLE);
                memberChar1.setVisibility(View.VISIBLE);
                memberChar2.setVisibility(View.VISIBLE);

                member1.setOnClickListener(goToUsers);
                member2.setOnClickListener(goToUsers);

                memberChar3.setText("+" + (data.users.length - 2));
                memberChar2.setText(data.users[0].Username.substring(0, 1));
                memberChar1.setText(data.users[1].Username.substring(0, 1));
            }
            else if (data.users.length == 3) {
                member1.setVisibility(View.VISIBLE);
                member2.setVisibility(View.VISIBLE);
                memberChar1.setVisibility(View.VISIBLE);
                memberChar2.setVisibility(View.VISIBLE);

                member1.setOnClickListener(goToUsers);
                member2.setOnClickListener(goToUsers);

                memberChar3.setText(data.users[2].Username.substring(0, 1));
                memberChar2.setText(data.users[0].Username.substring(0, 1));
                memberChar1.setText(data.users[1].Username.substring(0, 1));
            } else if (data.users.length == 2) {
                member2.setVisibility(View.VISIBLE);
                memberChar2.setVisibility(View.VISIBLE);

                member2.setOnClickListener(goToUsers);

                memberChar3.setText(data.users[0].Username.substring(0, 1));
                memberChar2.setText(data.users[1].Username.substring(0, 1));
            } else {
                memberChar3.setText(data.users[0].Username.substring(0, 1));
            }
        }

        scroll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void ShowNoTripsFragment(Context context)
    {
        ((AppCompatActivity)context).onBackPressed();
        Helpers.loadFragment(new NoTripsFragment(), ((AppCompatActivity)context).getSupportFragmentManager());

    }
}