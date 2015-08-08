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
import android.widget.TextView;
import android.widget.Toast;

import com.madproject.proctorbook.LoginActivity;
import com.madproject.proctorbook.MainActivity;
import com.madproject.proctorbook.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends Activity {

    Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
getActionBar().hide();
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        Button registerBtn = (Button) findViewById(R.id.registerButton);
        TextView cancel = (TextView)findViewById(R.id.cacelLabel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this,"Fill all fields Correctly",Toast.LENGTH_SHORT).show();

                } else {

                    RegisterActivity.this.progressBar = ProgressDialog.show(RegisterActivity.this, "", "Please Wait !! Signing You", true);

                    ParseUser user = new ParseUser();

                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());
                    //Very bad practice
                    user.put("pass", password.getText().toString());
                    user.setEmail(email.getText().toString());

                    user.put("type", "teacher");
                    user.put("display_name", username.getText().toString());


                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            RegisterActivity.this.progressBar.dismiss();
                            if (e == null) {
                                //Success
                                Intent success = new Intent(RegisterActivity.this, MainActivity.class);
                                success.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                success.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(success);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                RegisterActivity.this.progressBar.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
