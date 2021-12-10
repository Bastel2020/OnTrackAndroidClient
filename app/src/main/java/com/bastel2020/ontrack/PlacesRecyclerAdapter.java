package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> placeNames;
    private List<String> placeAddresses;
    private int[] placeIds;
    private ItemClickListener mClickListener;
    PlacesRecyclerAdapter(Context context, List<String> placeNames, List<String> placeAddresses, int[] placeIds)
    {
        mInflater = LayoutInflater.from(context);
        this.placeNames = placeNames;
        this.placeAddresses = placeAddresses;
        this.placeIds = placeIds;
    }


    @NonNull
    @Override
    public PlacesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.place_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }
        String placeName = placeNames.get(position);
        String placeAddress = placeAddresses.get(position);
        holder.nameField.setText(placeName);
        holder.placeAddressField.setText(placeAddress);

        if (FavoritesLogic.ContainsPlace(placeIds[position]))
            holder.favoritesButton.setBackgroundResource(R.drawable.favorite_checked);
        else
            holder.favoritesButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

        holder.favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoritesLogic.ChangeFavoritesButtonState(v.getContext(), holder.favoritesButton, placeIds[position], placeNames.get(position), placeAddresses.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameField, placeAddressField;
        ImageButton placePicture, favoritesButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.placeName_label);
            placeAddressField = itemView.findViewById(R.id.placeAdress_label);
            placePicture = itemView.findViewById(R.id.placeImage_button);
            favoritesButton = itemView.findViewById(R.id.AddPlaceToFavsSmall_button);


            ItemClickListener goToPlaceItemClick = new ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    loadFragment(PlaceFragment.newInstance(placeNames.get(position), placeIds[position]), (AppCompatActivity)view.getContext());
                }
            };

            placePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(PlaceFragment.newInstance(placeNames.get(getAdapterPosition()), placeIds[getAdapterPosition()]), (AppCompatActivity)v.getContext());
                }
            });
            mClickListener = goToPlaceItemClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return placeNames.get(id);
        }

        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            mClickListener = itemClickListener;
        }

    }

    public void loadFragment(Fragment fragment, AppCompatActivity activity) {
        // load fragment
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.default_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
