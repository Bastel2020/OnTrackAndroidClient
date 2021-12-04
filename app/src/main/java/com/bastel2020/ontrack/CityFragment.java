package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int cityId;
    private static PlaceCategoryRecyclerAdapter testAdapter;
    private static View view;

    public CityFragment() {
        // Required empty public constructor
    }

    public CityFragment(int cityId)
    {
        this.cityId = cityId;
    }

    // TODO: Rename and change types and number of parameters
    public static CityFragment newInstance(String param1, String param2) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        view = inflater.inflate(R.layout.fragment_city, container, false);

        ServerRequester.GetCityPlaces(view.getContext(), cityId);

        return view;
    }

    public static void UpdateEntities(List<ServerRequester.PlaceCategoryShortInfo> data)
    {
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<List<String>> placesNames = new ArrayList<List<String>>();
        ArrayList<List<String>> placesTextAddresses = new ArrayList<List<String>>();
        for (ServerRequester.PlaceCategoryShortInfo itVar: data
             ) {
            categories.add(itVar.CategoryName);
            ArrayList<String> tempPlaces = new ArrayList<String>();
            ArrayList<String> tempAddresses = new ArrayList<String>();

            for (ServerRequester.PlaceShortInfo place: itVar.Places
                 ) {
                tempPlaces.add(place.Name);
                tempAddresses.add(place.TextLocation);
            }

            placesNames.add(tempPlaces);
            placesTextAddresses.add(tempAddresses);
        }

        RecyclerView recyclerView = view.findViewById(R.id.cityPlaces_recycle);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalManager);
        testAdapter = new PlaceCategoryRecyclerAdapter(view.getContext(), categories, placesNames, placesTextAddresses);
        recyclerView.setAdapter(testAdapter);

        ProgressBar progressBar = view.findViewById(R.id.cityFragment_progressBar);
        progressBar.setVisibility(view.INVISIBLE);
    }
}