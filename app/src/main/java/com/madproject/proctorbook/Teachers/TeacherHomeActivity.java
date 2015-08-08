package com.madproject.proctorbook.Teachers;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.madproject.proctorbook.LoginActivity;
import com.madproject.proctorbook.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class TeacherHomeActivity extends ListActivity {

    List<ParseObject> mStudents;
    ArrayList<String> StudentNames = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_teacher_home);
        ParseUser CurrentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
        query.whereEqualTo("type", "student");
        query.whereEqualTo("proctor",CurrentUser.getObjectId());
        setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> students, ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if(e == null){
                    mStudents = students;
                    Log.i("students", mStudents.toString());
                    String[] studentNames  = new String[mStudents.size()];
                    int i =0;
                    for(ParseObject message: mStudents){
                        studentNames[i] = message.getString("display_name");
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            studentNames
                    );
                    setListAdapter(adapter);
                }else {
                    Toast.makeText(TeacherHomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
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
            Intent newStudent =  new Intent(TeacherHomeActivity.this, AddNewStudent.class);
            startActivity(newStudent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String displayName = mStudents.get(position).get("display_name").toString();
        String objectId = mStudents.get(position).getObjectId();

        Toast.makeText(TeacherHomeActivity.this,"Details of "+ displayName,Toast.LENGTH_SHORT).show();

        Intent detailActivity = new Intent(TeacherHomeActivity.this,StudentDetailActivity.class);
        detailActivity.putExtra("student_object_id", objectId);
        detailActivity.putExtra("student_display_name", displayName);
        startActivity(detailActivity);

    }
}
