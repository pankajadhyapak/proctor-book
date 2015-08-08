package com.madproject.proctorbook.Teachers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.madproject.proctorbook.MainActivity;
import com.madproject.proctorbook.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class AddNewStudent extends Activity {
    Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        final EditText firstname = (EditText) findViewById(R.id.first_name);
        final EditText lastname = (EditText) findViewById(R.id.last_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText phone = (EditText) findViewById(R.id.phone_no);
        final EditText usn = (EditText) findViewById(R.id.usn);

        Button addNewStudent = (Button) findViewById(R.id.AddStudentButton);

        addNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstname.getText().toString().isEmpty() ||
                        lastname.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() ||
                        address.getText().toString().isEmpty() ||
                        phone.getText().toString().isEmpty() ||
                        usn.getText().toString().isEmpty()) {

                    Toast.makeText(AddNewStudent.this, "Fill All Fields Correctly", Toast.LENGTH_SHORT).show();

                } else {
                    AddNewStudent.this.progressBar = ProgressDialog.show(AddNewStudent.this, "", "Please Wait !! Registering Student", true);

                    ParseUser oldUser = ParseUser.getCurrentUser();
                    final String oldUsername = oldUser.getUsername();
                    final String oldPassword = oldUser.get("pass").toString();

                    final ParseUser user = new ParseUser();

                    user.setUsername(usn.getText().toString());
                    user.setPassword("123456");
                    user.setEmail(email.getText().toString());
                    user.put("type", "student");
                    user.put("proctor", oldUser.getObjectId());


                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                AddNewStudent.this.progressBar.dismiss();
                                ParseObject studentDetails = new ParseObject("StudentDetails");
                                studentDetails.put("first_name", firstname.getText().toString());
                                studentDetails.put("last_name", lastname.getText().toString());
                                studentDetails.put("address", address.getText().toString());
                                studentDetails.put("phone", phone.getText().toString());
                                String display_name = firstname.getText().toString() + " " + lastname.getText().toString();
                                user.put("display_name", display_name);
                                studentDetails.put("user_id", user);

                                studentDetails.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            AddNewStudent.this.progressBar.dismiss();
                                            try {
                                                ParseUser.logIn(oldUsername, oldPassword);
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            //Success
                                            Toast.makeText(AddNewStudent.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                                            Intent success = new Intent(AddNewStudent.this, MainActivity.class);
                                            startActivity(success);
                                        } else {
                                            // Sign up didn't succeed. Look at the ParseException
                                            // to figure out what went wrong
                                            progressBar.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewStudent.this);
                                            builder.setMessage(e.getMessage()).setTitle("oops ! Something went wrong").setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                });
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                progressBar.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewStudent.this);
                                builder.setMessage(e.getMessage()).setTitle("oops ! Something went wrong").setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                    });


                }
            }

        });


    }
}
