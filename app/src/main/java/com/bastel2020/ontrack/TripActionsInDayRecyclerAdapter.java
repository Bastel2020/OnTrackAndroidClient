package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TripActionsInDayRecyclerAdapter extends RecyclerView.Adapter<TripActionsInDayRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ServerRequester.TripAction[] allTripActions;
    private List<ServerRequester.TripAction> firstTripActions;
    private int tripDayId;
    private String tripDate;
    private ItemClickListener mClickListener;
    private Context context;
    TripActionsInDayRecyclerAdapter(Context context, ServerRequester.TripAction[] allTripActions, int tripDayId, String tripDate)
    {
        mInflater = LayoutInflater.from(context);
        this.allTripActions = allTripActions;
        this.tripDayId = tripDayId;
        this.tripDate = tripDate;
        firstTripActions = new ArrayList<ServerRequester.TripAction>();
        if (allTripActions.length == 0)
        {
            firstTripActions.add(new ServerRequester.TripAction());
        }
        else
            firstTripActions.addAll(Arrays.asList(allTripActions));
        this.context = context;
        firstTripActions.add(new ServerRequester.TripAction());
    }


    @NonNull
    @Override
    public TripActionsInDayRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trip_action_string_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripActionsInDayRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }

        if (firstTripActions.get(position) == null || firstTripActions.get(position).Name == null)
            holder.tripActionName.setText(R.string.emptyActionName_label);
        else
            holder.tripActionName.setText(firstTripActions.get(position).Name);


        if (firstTripActions.get(position).Name == "")
            holder.tripActionName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TextView t = v.findViewById(R.id.tripActionName_label);
                    String s = t.getText().toString();
                    if (!hasFocus && s != null && s.length() > 0)
                        loadFragment(new EditActionFragment(s, tripDate, tripDayId), (AppCompatActivity)v.getContext());
                    else
                        Toast.makeText(v.getContext(), "Введите название события и нажмите Enter, чтобы перейти в меню подробного редактирования.", Toast.LENGTH_LONG);
                }
            });
        else
            holder.tripActionName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        loadFragment(new EditActionFragment(allTripActions[position].Id, tripDate), (AppCompatActivity)v.getContext());
                }
            });
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
        return firstTripActions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tripActionName;

        ViewHolder(View itemView) {
            super(itemView);

            tripActionName = itemView.findViewById(R.id.tripActionName_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return allTripActions[id].Name;
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
