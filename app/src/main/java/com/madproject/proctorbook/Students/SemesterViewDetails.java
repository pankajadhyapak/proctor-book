package com.madproject.proctorbook.Students;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.madproject.proctorbook.SubjectListItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.madproject.proctorbook.ProctorBook.getCurrentUserType;

public class SemesterViewDetails extends ListActivity {
    List<ParseObject> mSubjects;
    ArrayList<String> mSubjectIds;
    ArrayList<String> mSubjectMarks;
    MenuItem clearGrades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_semester_view_details);

        getSubjectsAndMarks();
        if(isAllGradesAdded()){
            TextView temp = (TextView)findViewById(R.id.noteMsg);
            temp.setText("All Grades are Added");
            temp.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        Intent semNoIntent = getIntent();
        final int sem_no = semNoIntent.getIntExtra("sem_no", 1);
        final String sem_obj = semNoIntent.getStringExtra("sem_obj");

        if(getCurrentUserType().equals("student")){

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                if (isAllGradesAdded()) {
                    Toast.makeText(SemesterViewDetails.this, "Grades are already Added ", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    final CharSequence[] items = {"S", "A", "B", "C", "D", "E", "X", "F"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(SemesterViewDetails.this);
                    builder.setTitle("Select Your Grade");
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, final int item) {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Semester");
                            query.getInBackground(sem_obj, new GetCallback<ParseObject>() {
                                public void done(ParseObject semester, ParseException e) {
                                    if (e == null) {
                                        semester.add("marks", items[item]);
                                        semester.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Toast.makeText(SemesterViewDetails.this, "Grade Updated !!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });


                        }


                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            }
        });
        }


    }

    private boolean isAllGradesAdded() {
        return mSubjectMarks.size() == mSubjectIds.size();
    }

    private void getSubjectsAndMarks() {
        Intent semNoIntent = getIntent();
        int sem_no = semNoIntent.getIntExtra("sem_no", 1);
        final String sem_obj = semNoIntent.getStringExtra("sem_obj");
        setProgressBarIndeterminateVisibility(true);
        mSubjectIds = semNoIntent.getStringArrayListExtra("sub_ids");
        mSubjectMarks = semNoIntent.getStringArrayListExtra("sub_grades");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Subjects");
        query.whereContainedIn("objectId", mSubjectIds);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> subjects, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    mSubjects = subjects;
                    final String[] subjectNames = new String[mSubjects.size()];
                    final String[] subjectCode = new String[mSubjects.size()];
                    final String[] subjectGrades = new String[mSubjects.size()];
                    final String[] subjectIds = new String[mSubjects.size()];
                    int i = 0;
                    for (ParseObject message : mSubjects) {
                        if (isAllGradesAdded()) {
                            subjectNames[i] = message.get("name").toString();
                            subjectCode[i] = message.get("code").toString();
                            subjectGrades[i] = mSubjectMarks.get(i);
                        } else {
                            subjectNames[i] = message.get("name").toString();
                            subjectCode[i] = message.get("code").toString();
                            subjectGrades[i] = "-";
                        }
                        subjectIds[i] = message.getObjectId();
                        i++;
                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                            SemesterViewDetails.this,
//                            android.R.layout.simple_list_item_1,
//                            subjectNames);
                    SubjectListItem adapter = new SubjectListItem(SemesterViewDetails.this,subjectNames,subjectCode,subjectGrades);

                    setListAdapter(adapter);

                } else {
                    Toast.makeText(SemesterViewDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_semester_view_details, menu);
        clearGrades = menu.getItem(0);
        if(getCurrentUserType().equals("teacher")){
            clearGrades.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_grades) {
            Intent semNoIntent = getIntent();
            final String sem_obj = semNoIntent.getStringExtra("sem_obj");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Semester");
            query.getInBackground(sem_obj,new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.remove("marks");
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(SemesterViewDetails.this,"Cleared Grades !! Add once Again",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });


        }

        return super.onOptionsItemSelected(item);
    }
}
