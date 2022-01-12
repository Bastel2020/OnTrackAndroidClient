package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoTripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoTripsFragment extends Fragment {

    private static AppCompatButton createButton, joinByCodeButton;
    private static TextView enteredCode;

    public NoTripsFragment() {
        // Required empty public constructor
    }

    public static NoTripsFragment newInstance() {
        NoTripsFragment fragment = new NoTripsFragment();
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
        View v = inflater.inflate(R.layout.fragment_no_trips, container, false);

        createButton = v.findViewById(R.id.createFirstTrip_button);
        joinByCodeButton = v.findViewById(R.id.joinByCode_labelButton);
        enteredCode = v.findViewById(R.id.joinByCode_Input);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loadFragment(new CreateTripFragment(), ((AppCompatActivity)v.getContext()).getSupportFragmentManager());
            }
        });

        joinByCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enteredCode != null && enteredCode.getText() != null && enteredCode.getText().toString() != null && enteredCode.getText().toString() != "")
                    ServerRequester.JoinToTripByCode(v.getContext(), enteredCode.getText().toString());
            }
        });

        return v;
    }
}