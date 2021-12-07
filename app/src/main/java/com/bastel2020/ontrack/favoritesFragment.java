package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PlaceCategoryRecyclerAdapter testAdapter;

    public favoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favoritesFragment newInstance(String param1, String param2) {
        favoritesFragment fragment = new favoritesFragment();
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
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Достопримечательности");
        categories.add("Парки");
        categories.add("Торговые центры");
        ArrayList<String> dost = new ArrayList<>();
        dost.add("Достопримечательность 1");
        dost.add("Достопримечательность 2");
        dost.add("Достопримечательность 3");
        ArrayList<String> parks = new ArrayList<>();
        parks.add("Парк 1");
        parks.add("Парк 2");
        parks.add("Парк 3");
        ArrayList<String> malls = new ArrayList<>();
        malls.add("Торговый центр 1");
        malls.add("Торговый центр 2");
        malls.add("Торговый центр 3");
        ArrayList<String> placeAddressess = new ArrayList<>();
        placeAddressess.add("Адрес 1");
        placeAddressess.add("Адрес 2");
        placeAddressess.add("Адрес 3");

        ArrayList<List<String>> allPlaces = new ArrayList<List<String>>();
        allPlaces.add(dost);
        allPlaces.add(parks);
        allPlaces.add(malls);

        ArrayList<List<String>> allAddresses = new ArrayList<List<String>>();
        allAddresses.add(placeAddressess);
        allAddresses.add(placeAddressess);
        allAddresses.add(placeAddressess);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.testRecycle);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalManager);
        testAdapter = new PlaceCategoryRecyclerAdapter(v.getContext(), 1, categories, allPlaces, allAddresses);
        recyclerView.setAdapter(testAdapter);
        return v;
    }
}