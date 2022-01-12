package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class TripDaysRecyclerAdapter extends RecyclerView.Adapter<TripDaysRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ServerRequester.TripDayInfo[] tripDays;
    private ItemClickListener mClickListener;
    private Context context;
    TripDaysRecyclerAdapter(Context context, ServerRequester.TripDayInfo[] tripDayInfos)
    {
        mInflater = LayoutInflater.from(context);
        this.tripDays = tripDayInfos;
        this.context = context;
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
        holder.dayOfWeekLabel.setText(tripDays[position].DayOfWeek);

        LinearLayoutManager verticalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
            return false; }
        };
        holder.actionsInDayRecycler.setLayoutManager(verticalManager);
        TripActionsInDayRecyclerAdapter adapter;
        if (tripDays[position].Actions.length > 4)
             adapter = new TripActionsInDayRecyclerAdapter(context, Arrays.copyOfRange(tripDays[position].Actions, 0, 4), tripDays[position].Id, tripDays[position].Date);
        else
            adapter = new TripActionsInDayRecyclerAdapter(context, tripDays[position].Actions, tripDays[position].Id, tripDays[position].Date);

        holder.actionsInDayRecycler.setAdapter(adapter);

        holder.expandActionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripActionsInDayRecyclerAdapter adapter;
                if (tripDays[position].Actions.length > 4) {
                    adapter = new TripActionsInDayRecyclerAdapter(context, tripDays[position].Actions, tripDays[position].Id, tripDays[position].Date);
                    holder.actionsInDayRecycler.setAdapter(adapter);
                }
            }
        });
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

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
