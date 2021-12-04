package com.bastel2020.ontrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceCategoryRecyclerAdapter extends RecyclerView.Adapter<PlaceCategoryRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> categoryName;
    private List<List<String>> placeNames;
    private List<List<String>> placeAddresses;
    private ItemClickListener mClickListener;
    private Context context;
    PlaceCategoryRecyclerAdapter(Context context, List<String> categoryName, List<List<String>> placeNames, List<List<String>> placeAddresses)
    {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.categoryName = categoryName;
        this.placeNames = placeNames;
        this.placeAddresses = placeAddresses;
    }


    @NonNull
    @Override
    public PlaceCategoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.placecategory_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceCategoryRecyclerAdapter.ViewHolder holder, int position) {
        holder.placeCategory.setText(categoryName.get(position));

        LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.placesRecycle.setLayoutManager(horizontalManager);
        PlacesRecyclerAdapter adapter = new PlacesRecyclerAdapter(context, placeNames.get(position), placeAddresses.get(position));
        holder.placesRecycle.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return categoryName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView placeCategory;
        RecyclerView placesRecycle;
        ViewHolder(View itemView) {
            super(itemView);
            placeCategory = itemView.findViewById(R.id.placeCategoryName_label);
            placesRecycle = itemView.findViewById(R.id.categoryItems_recycle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return categoryName.get(id);
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
