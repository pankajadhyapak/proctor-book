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
public class SemesterDetails extends ListFragment {
    List<ParseObject> mSemesters;
    ArrayList mRegSems = new ArrayList();

    public SemesterDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_semester_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Button addNewSemester = (Button) getView().findViewById(R.id.addNewSemesterButton);

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
            String oId = oid.getStringExtra("student_object_id");
            query.whereEqualTo("student_id", oId);
            addNewSemester.setVisibility(View.INVISIBLE);
        }else {
            query.whereEqualTo("student_id", ParseUser.getCurrentUser().getObjectId());

        }
        query.addAscendingOrder("semester_no");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> semesterObj, ParseException e) {
                //getActivity().setProgressBarIndeterminateVisibility(false);
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

                    if ((semesterObj.size() + 1) <= 6) {
                        addNewSemester.setText("Register for Semester " + (semesterObj.size() + 1));
                        addNewSemester.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent newSemReg = new Intent(getActivity(), AddNewSemesterActivity.class);
                                newSemReg.putExtra("selected_sem", Integer.toString((semesterObj.size() + 1)));
                                startActivity(newSemReg);
                            }
                        });
                    } else {
                        addNewSemester.setText("You Have Registered for all Semesters");
                        addNewSemester.setEnabled(false);
                    }
                }
            }
        });


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent SamViewDetails = new Intent(getActivity(), SemesterViewDetails.class);
        SamViewDetails.putExtra("sem_no", position + 1);
        SamViewDetails.putExtra("sem_obj", mSemesters.get(position).getObjectId());
        SamViewDetails.putStringArrayListExtra("sub_ids", new ArrayList(mSemesters.get(position).getList("subject_ids")));
        if(mSemesters.get(position).has("marks")){
            SamViewDetails.putStringArrayListExtra("sub_grades", new ArrayList(mSemesters.get(position).getList("marks")));
        }else {
            SamViewDetails.putStringArrayListExtra("sub_grades", new ArrayList());
        }
        startActivity(SamViewDetails);

    }
}
