package com.madproject.proctorbook.Students;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Calendar;

public class AddMeeting extends Activity {
    String Sdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
    }


    public void selectDate(View v) {

        final Button selectDate = (Button) findViewById(R.id.selectDate);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Sdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        selectDate.setText(Sdate);
                        Button com = (Button) findViewById(R.id.complaints);
                        com.setEnabled(true);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();
    }


    public void addComplaint(View v) {
        EditText complaint = (EditText) findViewById(R.id.complaintEditText);

        if (complaint.getText().toString().isEmpty()) {
            Toast.makeText(AddMeeting.this, "Complaint cannot be empty", Toast.LENGTH_SHORT).show();

        } else {
            String compText = complaint.getText().toString();
            Intent old = getIntent();
            int Sem = old.getIntExtra("sem_no", 1);
            String objId = old.getStringExtra("obj_id");

            ParseObject newMeeting = new ParseObject("Meetings");
            newMeeting.put("sem_no", Sem);
            newMeeting.put("student_id", objId);
            newMeeting.put("complaint", compText);
            newMeeting.put("date", Sdate);
            newMeeting.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(AddMeeting.this, "Complaint Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });


        }
    }
}
