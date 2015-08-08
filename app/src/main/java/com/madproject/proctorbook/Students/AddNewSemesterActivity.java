package com.madproject.proctorbook.Students;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewSemesterActivity extends FragmentActivity {

    final ArrayList seletedItems = new ArrayList();
    final ArrayList seletedSubjects = new ArrayList();
    Button termStartDate, termEndDate;
    int sYear, sMonth, sDay;
    int eYear, eMonth, eDay;
    String startDate, endDate;
    String seme;
    AlertDialog dialog;
    List<ParseObject> mSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_semester);

        TextView registerLabel = (TextView) findViewById(R.id.newRegSem);

        Intent AddNewSemester = getIntent();
        String sem = AddNewSemester.getStringExtra("selected_sem");
        seme = sem;
        registerLabel.setText("Register For " + sem + " Semester");
        termEndDate = (Button) findViewById(R.id.termEndDate);
        Button regNewSem = (Button) findViewById(R.id.regNewSem);
        regNewSem.setEnabled(false);
        termEndDate.setEnabled(false);
    }

    public void selectSubject(View v) {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Subjects");
        query.whereEqualTo("semester", Integer.parseInt(seme));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> subjects, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    mSubjects = subjects;
                    final String[] subjectNames = new String[mSubjects.size()];
                    int i = 0;
                    for (ParseObject message : mSubjects) {
                        subjectNames[i] = message.getString("code") + " - " + message.getString("name");
                        i++;
                    }
                    // arraylist to keep the selected items
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewSemesterActivity.this);
                    builder.setTitle("Select The Subjects");
                    seletedSubjects.clear();
                    seletedItems.clear();

                    builder.setMultiChoiceItems(subjectNames, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int indexSelected,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        seletedItems.add(subjectNames[indexSelected]);
                                        seletedSubjects.add(indexSelected);
                                    } else if (seletedItems.contains(subjectNames[indexSelected])) {
                                        // Else, if the item is already in the array, remove it
                                        seletedItems.remove(subjectNames[Integer.valueOf(indexSelected)]);
                                        seletedSubjects.remove(Integer.valueOf(indexSelected));
                                    }
                                }
                            })
                            // Set the action buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    ListView list = (ListView) findViewById(R.id.listView);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNewSemesterActivity.this,
                                            android.R.layout.simple_list_item_1, seletedItems);
                                    list.setAdapter(adapter);
                                    Button regNewSem = (Button) findViewById(R.id.regNewSem);

                                    if (seletedItems.size() > 0) {
                                        regNewSem.setEnabled(true);
                                    } else {
                                        regNewSem.setEnabled(false);
                                    }

                                    //Toast.makeText(AddNewSemesterActivity.this,seletedItems.toString(),Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on Cancel
                                    dialog.dismiss();

                                }
                            });

                    dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(AddNewSemesterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void termEndDate(View v) {
        int mYear, mMonth, mDay, mHour, mMinute;
        termEndDate = (Button) findViewById(R.id.termEndDate);
        final Calendar c = Calendar.getInstance();


        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        eDay = dayOfMonth;
                        eMonth = monthOfYear;
                        eYear = year;
                        // Display Selected date in textbox
                        endDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        termEndDate.setText(endDate);

                    }
                }, sYear, sMonth + 4, sDay);
//        Calendar c1 = Calendar.getInstance();
//        c1.add(Calendar.DATE, 120);
//        Date newDate2 = c1.getTime();
//        dpd.getDatePicker().setMinDate(newDate2.getTime());


        dpd.show();
    }

    public void termStartDate(View v) {
        termStartDate = (Button) findViewById(R.id.termStartDate);
        termEndDate = (Button) findViewById(R.id.termEndDate);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        sDay = dayOfMonth;
                        sMonth = monthOfYear;
                        sYear = year;
                        startDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        termStartDate.setText(startDate);
                        termEndDate.setEnabled(true);

                    }
                }, mYear, mMonth, mDay);

//        Calendar c1 = Calendar.getInstance();
//        c1.add(Calendar.DATE, -10);
//        Date newDate2 = c1.getTime();
//        c1.add(Calendar.DATE, 30);
//        Date newDate3 = c1.getTime();
//        dpd.getDatePicker().setMinDate(newDate2.getTime());
//        dpd.getDatePicker().setMaxDate(newDate3.getTime());

        dpd.show();
    }

    public void addNewSem(View v) {

        ParseObject semester = new ParseObject("Semester");
        semester.put("student_id", ParseUser.getCurrentUser().getObjectId());
        semester.put("semester_no", Integer.parseInt(seme));
        ArrayList<String> SubIds = new ArrayList<String>();

        for (int i = 0; i < seletedSubjects.size(); i++) {
            SubIds.add(mSubjects.get(i).getObjectId());
        }
        semester.put("subject_ids", SubIds);
        semester.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(AddNewSemesterActivity.this, "Semester Registered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNewSemesterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

}
