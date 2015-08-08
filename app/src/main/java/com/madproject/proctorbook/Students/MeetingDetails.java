package com.madproject.proctorbook.Students;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.madproject.proctorbook.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.madproject.proctorbook.ProctorBook.getCurrentUserType;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingDetails extends ListFragment {

    List<ParseObject> mSemesters;
    ArrayList mRegSems = new ArrayList();
    String objId;

    public MeetingDetails() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_details, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] semesters = new String[6];
        semesters[0] = "First Semester";
        semesters[1] = "Second Semester";
        semesters[2] = "Third Semester";
        semesters[3] = "Fourth Semester";
        semesters[4] = "Fifth Semester";
        semesters[5] = "Sixth Semester";

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Semester");
        if (getCurrentUserType().equals("teacher")) {

            Intent oid = getActivity().getIntent();
            objId = oid.getStringExtra("student_object_id");

        }else {
            objId = ParseUser.getCurrentUser().getObjectId();

        }
        query.whereEqualTo("student_id", objId);
        query.addAscendingOrder("semester_no");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> semesterObj, ParseException e) {
                if (e == null) {

                    mSemesters = semesterObj;
                    final String[] teacherNames = new String[mSemesters.size()];
                    int i = 0;
                    for (ParseObject message : mSemesters) {
                        mRegSems.add(semesters[(message.getNumber("semester_no").intValue()) - 1]);
                        teacherNames[i] = semesters[(message.getNumber("semester_no").intValue()) - 1];
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            teacherNames);
                    setListAdapter(adapter);

                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent SamViewDetails = new Intent(getActivity(), MeetingViewDetails.class);
        SamViewDetails.putExtra("sem_no",position+1);
        SamViewDetails.putExtra("object_id",objId);
        startActivity(SamViewDetails);

    }
}
