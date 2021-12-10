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
    private PlacesBigRecyclerAdapter testAdapter;

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

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.favoritesRecycle);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalManager);

        List<DbContext.TripLiteModel> data = FavoritesLogic.GetFavorites();
        List<String> tripNames = new ArrayList<String>();
        List<String> tripAddresses = new ArrayList<String>();
        int[] tripIds = new int[data.size()];

        int counter = 0;
        for (DbContext.TripLiteModel itVar: data)
        {
            tripNames.add(itVar.Name);
            tripAddresses.add(itVar.TextLocation);

            tripIds[counter] = itVar.Id;
            counter++;
        }

        testAdapter = new PlacesBigRecyclerAdapter(v.getContext(), tripNames, tripAddresses, tripIds);
        recyclerView.setAdapter(testAdapter);
        return v;
    }
}