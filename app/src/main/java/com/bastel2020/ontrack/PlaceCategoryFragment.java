package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceCategoryFragment extends Fragment {

    private String categoryName;
    private int cityId, categoryId;
    private static PlacesBigRecyclerAdapter testAdapter;
    private static View view;

    public PlaceCategoryFragment() { }
    public PlaceCategoryFragment(String categoryName, int cityId, int categoryId) {
        this.categoryName = categoryName;
        this.cityId = cityId;
        this.categoryId = categoryId;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place_category, container, false);

        TextView text = view.findViewById(R.id.placeCategoryPage_label);
        text.setText(categoryName);

        ServerRequester.GetCategoryPlaces(view.getContext(), cityId, categoryId);

        return view;
    }

    public static void UpdateEntities(ServerRequester.PlaceShortInfo[] data)
    {
        ArrayList<String> placesNames = new ArrayList<String>();
        ArrayList<String> placesTextAddresses = new ArrayList<String>();
        for (ServerRequester.PlaceShortInfo itVar: data
        ) {
            placesNames.add(itVar.Name);
            placesTextAddresses.add(itVar.TextLocation);
        }

        RecyclerView recyclerView = view.findViewById(R.id.placeCategory_reycle);
        LinearLayoutManager verticalManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        testAdapter = new PlacesBigRecyclerAdapter(view.getContext(), placesNames, placesTextAddresses);
        recyclerView.setAdapter(testAdapter);

        ProgressBar progressBar = view.findViewById(R.id.placeCategory_progressBar);
        progressBar.setVisibility(view.INVISIBLE);
    }
}