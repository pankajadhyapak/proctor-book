package com.madproject.proctorbook.Admin;

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
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class AdminSubjectsLists extends ListActivity {

    Integer sem_no;
    List<ParseObject> mSubjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_admin_subjects_lists);

        Intent ac = getIntent();
        sem_no = ac.getIntExtra("sem_no", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProgressBarIndeterminateVisibility(true);

        getSubjectForSemester();

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminSubjectsLists.this);
                alertDialogBuilder.setMessage("Are You Sure To Delete Subject ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mSubjects.get(pos).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(AdminSubjectsLists.this, "Subject Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            getSubjectForSemester();
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    private void getSubjectForSemester() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Subjects");
        query.whereEqualTo("semester", sem_no);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> teachers, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    mSubjects = teachers;
                    String[] teacherNames = new String[mSubjects.size()];
                    int i = 0;
                    for (ParseObject message : mSubjects) {
                        teacherNames[i] = message.getString("code") + " - " + message.getString("name");
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            AdminSubjectsLists.this,
                            android.R.layout.simple_list_item_1,
                            teacherNames
                    );
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(AdminSubjectsLists.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_subjects_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sub) {
            Intent newSub = new Intent(AdminSubjectsLists.this, AdminAddSubject.class);
            newSub.putExtra("sem_no", sem_no);
            startActivity(newSub);
        }

        return super.onOptionsItemSelected(item);
    }


}
