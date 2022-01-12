package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTripFragment extends Fragment {

    private static EditText Name, startDate, endDate, joinCode;
    private static AppCompatButton CreateTripButton, JoinByCodeButton;
    private static ImageButton back;
    private static Spinner tripCity;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTripFragment newInstance(String param1, String param2) {
        CreateTripFragment fragment = new CreateTripFragment();
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
        View v = inflater.inflate(R.layout.fragment_create_trip, container, false);

        Name = v.findViewById(R.id.newTripName_input);
        startDate = v.findViewById(R.id.newTripStartDate_input);
        endDate = v.findViewById(R.id.newTripEndDate_input);
        joinCode = v.findViewById(R.id.joinByCodeInCreate_Input);

        tripCity = v.findViewById(R.id.newTripCity_list);

        back = v.findViewById(R.id.createTrip_back);

        CreateTripButton = v.findViewById(R.id.createFirstTrip_button);
        JoinByCodeButton = v.findViewById(R.id.joinByCodeInCreate_Button);

        tripCity.setAdapter(new ArrayAdapter<String>(v.getContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Екатеринбург", "Калининград", "Москва", "Казань", "Сочи", "Санкт-Петербург"}));
        tripCity.setPrompt("Выберите город");
        //tripCity.setSelection(0);

        CreateTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequester.CreateTrip(v.getContext(), new ServerRequester.CreateTrip(tripCity.getSelectedItemPosition() + 1, Name.getText().toString(), startDate.getText().toString(), endDate.getText().toString()));
            }
        });

        JoinByCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(joinCode != null && joinCode.getText() != null && joinCode.getText().toString() != null && joinCode.getText().toString() != "")
                    ServerRequester.JoinToTripByCode(v.getContext(), joinCode.getText().toString());
            }
        });

        back.setOnClickListener(Helpers.backButtonListener);

        return v;
    }

    public static void OnTripCreated(ServerRequester.TripInfo createdTrip, Context context)
    {
        AppCompatActivity activity = (AppCompatActivity)context;
        activity.onBackPressed();
        Helpers.loadFragment(new tripsFragment(context, createdTrip), activity.getSupportFragmentManager());
    }
}