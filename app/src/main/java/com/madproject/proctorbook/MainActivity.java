package com.madproject.proctorbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.madproject.proctorbook.Admin.AdminHomeActivity;
import com.madproject.proctorbook.Students.StudentHomeActivity;
import com.madproject.proctorbook.Teachers.AddNewStudent;
import com.madproject.proctorbook.Teachers.TeacherHomeActivity;
import com.parse.ParseUser;

import static com.madproject.proctorbook.ProctorBook.getCurrentUserType;


public class MainActivity extends Activity {

    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout);

        if (currentUser == null) {
            navigateToLoginActivity();
        } else {
            Toast.makeText(MainActivity.this,getCurrentUserType(),Toast.LENGTH_SHORT).show();
            if (getCurrentUserType().equals("teacher")) {
                ActivityStart(MainActivity.this, TeacherHomeActivity.class);
            } else if (getCurrentUserType().equals("student")) {
                ActivityStart(MainActivity.this, StudentHomeActivity.class);
            } else if (getCurrentUserType().equals("admin")) {
                ActivityStart(MainActivity.this, AdminHomeActivity.class);
            }
        }
    }


    private void ActivityStart(Context from, Class to) {

        Intent intent = new Intent(from, to);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void navigateToLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigateToLoginActivity();
        }

        if (id == R.id.action_add_new_student) {
            Intent newStudent = new Intent(MainActivity.this, AddNewStudent.class);
            startActivity(newStudent);
        }

        return super.onOptionsItemSelected(item);
    }

}
