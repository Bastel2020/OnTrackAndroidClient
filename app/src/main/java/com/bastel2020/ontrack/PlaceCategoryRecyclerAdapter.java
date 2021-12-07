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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PlaceCategoryRecyclerAdapter extends RecyclerView.Adapter<PlaceCategoryRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> categoryName;
    private int cityId;
    private List<List<String>> placeNames;
    private List<List<String>> placeAddresses;
    private ItemClickListener mClickListener;
    private Context context;
    private FragmentManager fragmentManager;
    PlaceCategoryRecyclerAdapter(Context context, int cityId, List<String> categoryName, List<List<String>> placeNames, List<List<String>> placeAddresses)
    {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.cityId = cityId;
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
        TextView test = holder.itemView.findViewById(R.id.showAll_label);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new PlaceCategoryFragment(categoryName.get(position), cityId, position), (AppCompatActivity)v.getContext());
                Log.d(TAG, "Clicked: " + position);
            }
        });
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

    public void loadFragment(Fragment fragment, AppCompatActivity activity) {
        // load fragment
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.default_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
