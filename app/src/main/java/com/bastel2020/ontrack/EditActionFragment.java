package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditActionFragment extends Fragment {

    private static TextView actionName, actionDate, actionTimeInput, actionNameInput, actionDescription;
    private static String newActionName, newActionDate;
    private static Button saveChanges;
    private AppCompatImageButton backButton;
    private static ProgressBar progressBar;
    private static ScrollView scroll;
    private static int editActionId, parentId;

    public EditActionFragment() {
        // Required empty public constructor
    }

    public EditActionFragment(String newActionName, String newActionDate, int parentId)
    {
        this.newActionName = newActionName;
        this.newActionDate = newActionDate;
        this.parentId = parentId;
    }

    public EditActionFragment(int editActionId, String newActionDate)
    {
        this.editActionId = editActionId;
        this.newActionDate = newActionDate;
        this.newActionName = null;
    }

    public static EditActionFragment newInstance() {
        EditActionFragment fragment = new EditActionFragment();
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
        View v = inflater.inflate(R.layout.fragment_edit_action, container, false);

        scroll = v.findViewById(R.id.editAction_scroll);
        scroll.setVisibility(View.INVISIBLE);

        backButton = v.findViewById(R.id.editActionBack_button);

        actionName = v.findViewById(R.id.actionName_label);
        actionNameInput = v.findViewById(R.id.actionName_edit);
        actionTimeInput = v.findViewById(R.id.actionTime_edit);
        actionDate = v.findViewById(R.id.actionDateInEdit_label);
        actionDescription = v.findViewById(R.id.actionDescription_edit);
        progressBar = v.findViewById(R.id.actionEdit_progressBar);
        saveChanges = v.findViewById(R.id.actionSaveChanges_button);

        backButton.setOnClickListener(Helpers.backButtonListener);

        if(newActionName != null && newActionName != "") {
            actionName.setText(newActionName);
            actionNameInput.setText(newActionName);
            actionDate.setText(newActionDate);

            saveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(parentId != 0 && actionNameInput.getText().toString() != "" && actionTimeInput.getText().toString() != "") {
                        ServerRequester.CreateNewAction(getContext(), new ServerRequester.CreateAction(parentId, actionNameInput.getText().toString(), actionDescription.getText().toString(), actionTimeInput.getText().toString()));
                        getActivity().onBackPressed();
                    }
                }
            });

            scroll.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else
        {
            saveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editActionId != 0 && actionNameInput.getText().toString() != "" && actionTimeInput.getText().toString() != ""){
                        ServerRequester.EditAction(getContext(), new ServerRequester.EditAction(editActionId, actionNameInput.getText().toString(), actionDescription.getText().toString(), actionTimeInput.getText().toString()));
                        getActivity().onBackPressed();
                    }
                }
            });
            ServerRequester.GetAction(getContext(), editActionId);
        }
        return  v;
    }

    public static void UpdateEntities(Context context, ServerRequester.TripAction data)
    {
        actionName.setText(data.Name);
        actionNameInput.setText(data.Name);
        actionTimeInput.setText(data.TimeOfAction);
        actionDescription.setText(data.Description);
        actionDate.setText(newActionDate);

        scroll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }
}