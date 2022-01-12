package com.bastel2020.ontrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {

    private static View v;

    public static PlaceFragment newInstance(String placeName, int placeId) {
        PlaceFragment fragment = new PlaceFragment();

        Bundle args = new Bundle();
        args.putString("placeName", placeName);
        args.putInt("placeId", placeId);

        //TextView name = v.findViewById(R.id.placeName_text);
        //name.setText(placeName);
        ServerRequester.GetPlaceInfo(v, placeId);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("placeName")) {
                TextView name = getView().findViewById(R.id.placeName_text);
                name.setText(savedInstanceState.getString("placeName"));
            }
            if (savedInstanceState.containsKey("placeId")) {
                ServerRequester.GetPlaceInfo(getView(), savedInstanceState.getInt("placeId"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_place, container, false);
        AppCompatImageButton backButton = v.findViewById(R.id.placeBack_button);
        backButton.setOnClickListener(Helpers.backButtonListener);
        return v;
    }

    public static void UpdateEntities(View view, ServerRequester.PlaceInfo data)
    {
        TextView name = v.findViewById(R.id.placeName_text);
        TextView address = v.findViewById(R.id.placeAddress_text);
        TextView workingHours = v.findViewById(R.id.placeWorkingHours_text);
        TextView enterCost = v.findViewById(R.id.placeEnterCost_text);
        TextView placeSite = v.findViewById(R.id.placeSite_text);
        TextView description = v.findViewById(R.id.placeDescription_text);
        ProgressBar progressBar = v.findViewById(R.id.place_progressBar);

        ImageButton addToFavorites = v.findViewById(R.id.place_favorites_button);

        if (FavoritesLogic.ContainsPlace(data.Id))
            addToFavorites.setBackgroundResource(R.drawable.favorite_checked);
        else
            addToFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoritesLogic.ChangeFavoritesButtonState(v.getContext(), addToFavorites, data.Id, data.Name, data.TextLocation);
            }
        });

        name.setText(data.Name);
        address.setText(data.TextLocation);
        workingHours.setText(data.WorkingHours);
        enterCost.setText(data.EnterCost);
        placeSite.setText(v.getContext().getString(R.string.placeSite_label) + " " + data.PlaceSite);
        description.setText(data.Description);

        try {
            if (data.PlaceSite != null) {
                placeSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW, Uri.parse(data.PlaceSite));
                        ((AppCompatActivity) v.getContext()).startActivity(browserIntent);
                    }
                });
            }
            else
                placeSite.setText(v.getContext().getString(R.string.noPlaceSite_label));
        }
        catch(Exception e) {
            Log.e(TAG, "Can't open Url: " + data.PlaceSite + " Error:" + e.getMessage());}
        progressBar.setVisibility(View.INVISIBLE);
    }
}