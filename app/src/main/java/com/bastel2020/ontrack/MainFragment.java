package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private SearchView searchView;
    private ImageButton ekbButton, kaliningradButton, mskButton, kazanButton, sochiButton, spbButton;
    private FloatingActionButton createButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        createButton = view.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loadFragment(new CreateTripFragment(), ((AppCompatActivity)v.getContext()).getSupportFragmentManager());
            }
        });

//        searchView = view.findViewById(R.id.search_places);

        ekbButton = view.findViewById(R.id.ekbButton);
        kaliningradButton = view.findViewById(R.id.klnButton);
        mskButton = view.findViewById(R.id.mskButton);
        kazanButton = view.findViewById(R.id.kznButton);
        sochiButton = view.findViewById(R.id.sochiButton);
        spbButton = view.findViewById(R.id.spbButton);

        ekbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(1);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        kaliningradButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(2);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        mskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(3);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        kazanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(4);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        sochiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(5);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        spbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CityFragment(6);
                Helpers.loadFragment(fragment, getActivity().getSupportFragmentManager());
            }
        });

        return view;
    }
}