package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class TripsInProfileRecyclerAdapter extends RecyclerView.Adapter<TripsInProfileRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private List<Integer> tripsIds = new ArrayList<>();
    private List<String> tripsNames = new ArrayList<>();
    private List<String> tripsCities = new ArrayList<>();
    private List<String> tripsDates = new ArrayList<>();
    private List<Integer> placesCount = new ArrayList<>();
    private List<ServerRequester.UserInTripShortInfo[]> usersInRightMenu = new ArrayList<>();
    private List<Integer> additionalUsersCount = new ArrayList<>();
    TripsInProfileRecyclerAdapter(Context context, ServerRequester.TripShortInfo[] trips)
    {
        mInflater = LayoutInflater.from(context);
        for (ServerRequester.TripShortInfo trip: trips)
        {
            tripsIds.add(trip.Id);
            tripsNames.add(trip.Name);
            tripsCities.add(trip.DestinationName);
            tripsDates.add(trip.TripStart + " - " + trip.tripEnd);
            placesCount.add(trip.placesCount);
            usersInRightMenu.add(trip.firstUsers);
            additionalUsersCount.add(trip.AdditionalUserCount);
        }
    }


    @NonNull
    @Override
    public TripsInProfileRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trip_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsInProfileRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }

        holder.tripNamesField.setText(tripsNames.get(position));
        holder.tripCityField.setText(tripsCities.get(position));
        holder.tripDatesField.setText(tripsDates.get(position));

        if(additionalUsersCount.get(position) > 0)
        {
            holder.tripMembersThird.setVisibility(View.VISIBLE);
            holder.tripMembersSecond.setVisibility(View.VISIBLE);
            holder.tripMembersFirst.setVisibility(View.VISIBLE);
        }
        if (usersInRightMenu.get(position) != null && usersInRightMenu.get(position).length > 0)
        {
            if (usersInRightMenu.get(position).length > 1)
            {
                holder.tripMembersSecond.setVisibility(View.VISIBLE);
                holder.tripMembersFirst.setVisibility(View.VISIBLE);
                String x = usersInRightMenu.get(position)[0].Username.substring(0, 1);
                holder.tripMembersSecond.setBackground(new TextDrawable(usersInRightMenu.get(position)[0].Username.substring(0, 1)));
            }
            else
            {
                holder.tripMembersFirst.setVisibility(View.VISIBLE);
            }
        }

        int placesCountValue = placesCount.get(position);
        String placesCountLabel;

        if (placesCountValue % 10 == 0)
            placesCountLabel = placesCountValue + " мест";
        else if(placesCountValue % 10 == 1)
            placesCountLabel = placesCountValue + " место";
        else if(placesCountValue % 10 < 5)
            placesCountLabel = placesCountValue + " места";
        else
            placesCountLabel = placesCountValue + " мест";

        holder.tripPlacesCountField.setText(placesCountLabel);

        ItemClickListener goToTripItemClick = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadFragment(new tripsFragment(tripsNames.get(position), tripsCities.get(position), tripsDates.get(position), tripsIds.get(position)), (AppCompatActivity)view.getContext());
            }
        };

        holder.setClickListener(goToTripItemClick);
    }

    @Override
    public int getItemCount() {
        return tripsIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View v;
        TextView tripNamesField, tripDatesField, tripCityField, tripPlacesCountField;
        CircleImageView tripMembersFirst, tripMembersSecond, tripMembersThird;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tripNamesField = itemView.findViewById(R.id.tripNameInList_label);
            tripDatesField = itemView.findViewById(R.id.tripDatesInList_label);
            tripCityField = itemView.findViewById(R.id.tripCityInList_label);
            tripPlacesCountField = itemView.findViewById(R.id.tripPlacesCountInList_label);

            tripMembersFirst = itemView.findViewById(R.id.tripMembersInList1);
            tripMembersFirst.setVisibility(View.INVISIBLE);
            tripMembersSecond = itemView.findViewById(R.id.tripMembersInList2);
            tripMembersSecond.setVisibility(View.INVISIBLE);
            tripMembersThird = itemView.findViewById(R.id.tripMembersInList3);
            tripMembersThird.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return tripsNames.get(id);
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
