package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PlacesBigRecyclerAdapter extends RecyclerView.Adapter<PlacesBigRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> placeNames;
    private List<String> placeAddresses;
    private ItemClickListener mClickListener;
    PlacesBigRecyclerAdapter(Context context, List<String> placeNames, List<String> placeAddresses)
    {
        mInflater = LayoutInflater.from(context);
        this.placeNames = placeNames;
        this.placeAddresses = placeAddresses;
    }


    @NonNull
    @Override
    public PlacesBigRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.place_list_big_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesBigRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }
        String placeName = placeNames.get(position);
        String placeAddress = placeAddresses.get(position);
        holder.nameField.setText(placeName);
        holder.placeAddressField.setText(placeAddress);
    }

    @Override
    public int getItemCount() {
        return placeNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameField, placeAddressField;

        ViewHolder(View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.placeNameBig_label);
            placeAddressField = itemView.findViewById(R.id.placeAdressBig_label);
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

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
