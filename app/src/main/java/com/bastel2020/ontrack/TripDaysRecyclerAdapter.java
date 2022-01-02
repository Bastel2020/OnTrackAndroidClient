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

import static android.content.ContentValues.TAG;

public class TripDaysRecyclerAdapter extends RecyclerView.Adapter<TripDaysRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ServerRequester.TripDayInfo[] tripDays;
    private ItemClickListener mClickListener;
    TripDaysRecyclerAdapter(Context context, ServerRequester.TripDayInfo[] tripDays)
    {
        mInflater = LayoutInflater.from(context);
        this.tripDays = tripDays;
    }


    @NonNull
    @Override
    public TripDaysRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trip_action_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDaysRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }
        holder.dateLabel.setText(tripDays[position].Date);
        //holder.dayOfWeekLabel.setText(tripDays[position].Id);

//        holder.favoritesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FavoritesLogic.ChangeFavoritesButtonState(v.getContext(), holder.favoritesButton, placeIds[position], placeNames.get(position), placeAddresses.get(position));
//            }
//        });

//        holder.placePictureBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(PlaceFragment.newInstance(placeNames.get(position), placeIds[position]), (AppCompatActivity)v.getContext());
//            }
//        });

//        mClickListener = new TripDaysRecyclerAdapter.ItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                loadFragment(PlaceFragment.newInstance(placeNames.get(position), placeIds[position]), (AppCompatActivity)view.getContext());
//            }
//        };
    }

    @Override
    public int getItemCount() {
        return tripDays.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateLabel, dayOfWeekLabel;
        ImageButton expandActionsButton;
        RecyclerView actionsInDayRecycler;

        ViewHolder(View itemView) {
            super(itemView);

            dateLabel = itemView.findViewById(R.id.tripActionDate_label);
            dayOfWeekLabel = itemView.findViewById(R.id.tripActionDayOfWeek_label);
            expandActionsButton = itemView.findViewById(R.id.expandActions_button);
            actionsInDayRecycler = itemView.findViewById(R.id.actionsInDay_recycle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return tripDays[id].Date;
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
