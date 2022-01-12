package com.bastel2020.ontrack;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripMembersFragment extends Fragment {

    private static int tripId;
    private static TextView inviteCodeInput, generateNewCodeLabel;
    private static RecyclerView members;
    private static ScrollView scroll;
    private static ProgressBar progressBar;
    private static ImageButton generateNewCode, copyCode, back;
    private static ConstraintLayout inviteLayout;

    public TripMembersFragment() { }

    public TripMembersFragment(int tripId)
    {
        this.tripId = tripId;
    }

    public static TripMembersFragment newInstance(String param1, String param2) {
        TripMembersFragment fragment = new TripMembersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trip_members, container, false);

        scroll = v.findViewById(R.id.tripMembers_scroll);
        scroll.setVisibility(View.INVISIBLE);

        progressBar = v.findViewById(R.id.tripMembers_progressBar);

        inviteCodeInput = v.findViewById(R.id.generateInvite_input);

        copyCode = v.findViewById(R.id.copyCodeToClipboard_button);
        back = v.findViewById(R.id.tripMembersBack_button);
        back.setOnClickListener(Helpers.backButtonListener);

        generateNewCodeLabel = v.findViewById(R.id.generateNewCode_label);
        generateNewCode = v.findViewById(R.id.refreshCode_button);
        members = v.findViewById(R.id.tripMembersPage_recycle);
        inviteLayout = v.findViewById(R.id.inviteCode_layout);

        View.OnClickListener generateCode = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequester.GenerateInviteCode(v.getContext(), tripId);
            }
        };

        generateNewCode.setOnClickListener(generateCode);
        generateNewCodeLabel.setOnClickListener(generateCode);

        ServerRequester.GetTripInfo(v.getContext(), tripId, true);
        return v;
    }

    public static void UpdateEntities(Context context, ServerRequester.TripInfo trip)
    {
        int requesterRole = 3;
        for (ServerRequester.UserInTripInfo user: trip.users)
        {
            if (user.isCurrentUser)
                requesterRole = user.Role;
        }


        if(requesterRole <= trip.RequiedRoleForInviteCode) {
            if (trip.InviteCode != null && trip.InviteCode != "") {
                inviteCodeInput.setText(trip.InviteCode);

                copyCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("", inviteCodeInput.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(v.getContext(), R.string.copiedToClipboard_toast, Toast.LENGTH_LONG); //TODO: исправить нерабочий toast
                    }
                });
            }
        }
        else
        {
            inviteLayout.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager verticalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false; }
        };
        members.setLayoutManager(verticalManager);
        members.setAdapter(new TripMemberRecyclerAdapter(context, trip));

        //TO DO
        scroll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}