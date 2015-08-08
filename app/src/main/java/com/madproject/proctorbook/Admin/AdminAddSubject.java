package com.madproject.proctorbook.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class AdminAddSubject extends Activity {
    Integer sem_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_subject);
        TextView semLabel = (TextView)findViewById(R.id.semLabel);
        Intent addSub = getIntent();
        sem_no = addSub.getIntExtra("sem_no",1);
        semLabel.setText("Add New Subject for "+ Integer.toString(sem_no) + " Semester");
    }

    public void addNewSubject(View v){
        EditText subCode = (EditText)findViewById(R.id.subCode);
        EditText subName = (EditText)findViewById(R.id.subName);
        EditText subCredits = (EditText)findViewById(R.id.subCredits);

        String SubcodeS = subCode.getText().toString();
        String subNameS = subName.getText().toString();
        String subCreditsS = subCredits.getText().toString();

        if(SubcodeS.isEmpty() || subCreditsS.isEmpty() || subNameS.isEmpty()){
            Toast.makeText(this,"Fill All the fields Correctly",Toast.LENGTH_SHORT).show();
        }else {
            ParseObject subject = new ParseObject("Subjects");
            subject.put("code", SubcodeS);
            subject.put("name", subNameS);
            subject.put("credits", subCreditsS);
            subject.put("semester", sem_no);

            subject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(AdminAddSubject.this,"Subject Added Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_add_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
