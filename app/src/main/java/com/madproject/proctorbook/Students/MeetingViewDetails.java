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
import android.widget.ListView;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.madproject.proctorbook.ProctorBook.getCurrentUserType;

public class MeetingViewDetails extends ListActivity {
    Integer sem_no;
    String objId;
    List<ParseObject> mComplaints;
    MenuItem addCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_view_details);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent from = getIntent();
        sem_no = from.getIntExtra("sem_no", 1);
        objId = from.getStringExtra("object_id");
        getActionBar().setTitle("Meeting's of " + Integer.toString(sem_no) + " Semester");
        setProgressBarIndeterminateVisibility(true);

        getComplaints();

        if (getCurrentUserType().equals("student")) {


            getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingViewDetails.this);
                    alertDialogBuilder.setMessage("Are You Sure To Delete this meeting ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mComplaints.get(pos).deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(MeetingViewDetails.this, "Meeting Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                getComplaints();
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
    }

    private void getComplaints() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Meetings");
        if (getCurrentUserType().equals("student")) {
            objId = ParseUser.getCurrentUser().getObjectId();
        }
        query.whereEqualTo("student_id", objId);
        query.whereEqualTo("sem_no", sem_no);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> complaintObjects, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {

                    mComplaints = complaintObjects;
                    final String[] Complaints = new String[mComplaints.size()];
                    int i = 0;
                    for (ParseObject message : mComplaints) {
                        Complaints[i] = message.get("complaint").toString();
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            MeetingViewDetails.this,
                            android.R.layout.simple_list_item_1,
                            Complaints);

                    setListAdapter(adapter);


                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting_view_details, menu);
        addCom = menu.getItem(0);
        if (getCurrentUserType().equals("student")) {
            addCom.setVisible(true);
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
        if (id == R.id.action_meetings) {

            Intent addMeeting = new Intent(MeetingViewDetails.this, AddMeeting.class);
            addMeeting.putExtra("sem_no", sem_no);
            addMeeting.putExtra("obj_id", objId);
            startActivity(addMeeting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject com = mComplaints.get(position);

        String comDetails = "Created Date: " + com.get("date") + "\n" + "Complaint: " + com.get("complaint");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingViewDetails.this);
        alertDialogBuilder.setTitle("Complaint Details");
        alertDialogBuilder.setMessage(comDetails)
                .setCancelable(true)
                .setPositiveButton("ok", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
