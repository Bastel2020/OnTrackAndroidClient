package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripMembersFragment extends Fragment {

    private static ServerRequester.TripInfo trip;
    private static TextView inviteCodeInput, generateNewCodeLabel;
    private static RecyclerView members;
    private static Button generateNewCode;

    public TripMembersFragment() { }

    public TripMembersFragment(ServerRequester.TripInfo trip)
    {
        this.trip = trip;
    }

    public static TripMembersFragment newInstance(String param1, String param2) {
        TripMembersFragment fragment = new TripMembersFragment();
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
        View v = inflater.inflate(R.layout.fragment_trip_members, container, false);

        inviteCodeInput = v.findViewById(R.id.generateInvite_input);
        generateNewCodeLabel = v.findViewById(R.id.generateNewCode_label);
        members = v.findViewById(R.id.tripMembersPage_recycle);
        generateNewCode = v.findViewById(R.id.refreshCode_button);

        if(trip != null)
        {
            if (trip.InviteCode != null && trip.InviteCode != "")
                inviteCodeInput.setText(trip.InviteCode);

            //TO DO
        }

        return v;
    }
}