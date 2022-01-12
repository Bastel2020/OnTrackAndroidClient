package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class TripMemberRecyclerAdapter extends RecyclerView.Adapter<TripMemberRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    ServerRequester.TripInfo tripInfo;
    private int requesterRole;
    private ItemClickListener mClickListener;
    private Context context;
    TripMemberRecyclerAdapter(Context context, ServerRequester.TripInfo tripInfo)
    {
        mInflater = LayoutInflater.from(context);

        this.tripInfo = tripInfo;
        for (ServerRequester.UserInTripInfo user: tripInfo.users)
        {
            if (user.isCurrentUser)
                requesterRole = user.Role;
        }

        this.context = context;
    }


    @NonNull
    @Override
    public TripMemberRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trip_member_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripMemberRecyclerAdapter.ViewHolder holder, int position) {
        if (position + 1 > getItemCount()) {
            Log.e(TAG, "Try to bind element index out bounds array!");
            return;
        }

        if (tripInfo.users[position].isCurrentUser) {
            holder.memberName.setText("Вы: " + tripInfo.users[position].Username);
        }
        else
        {
            holder.memberName.setText(tripInfo.users[position].Username);
        }
        holder.memberChar.setText(tripInfo.users[position].Username.substring(0, 1));

        if(tripInfo.users[position].Role == 0) {
            holder.memberRole.setAdapter(new ArrayAdapter<String>(holder.v.getContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Создатель"}));
            holder.memberRole.setEnabled(false);
            return;
        }
        else if(requesterRole != 0) //Запрет на изменение ролей, если изменяет не создатель
            holder.memberRole.setEnabled(false);

        holder.memberRole.setSelection(tripInfo.users[position].Role - 1);
        holder.memberRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos != tripInfo.users[position].Role - 1)
                    ServerRequester.ChangeUserRole(view.getContext(), new ServerRequester.ChangeUserRole(tripInfo.Id, tripInfo.users[position].Id, pos + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tripInfo.users.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView memberName, memberChar;
        CircleImageView memberAvatar;
        Spinner memberRole;
        View v;

        ViewHolder(View itemView) {
            super(itemView);

            v = itemView;

            memberName = itemView.findViewById(R.id.listMemberName_label);
            memberChar = itemView.findViewById(R.id.listmemberChar);

            memberRole = itemView.findViewById(R.id.listMemberRole_list);

            memberAvatar = itemView.findViewById(R.id.listmemberCircle);

            memberRole.setAdapter(new ArrayAdapter<String>(itemView.getContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Редактор", "Участник"}));
            memberRole.setPrompt("Выберите роль");

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public String getItem(int id) {
            return tripInfo.users[id].Username;
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
