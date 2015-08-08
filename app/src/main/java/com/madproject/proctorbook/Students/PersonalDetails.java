package com.madproject.proctorbook.Students;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.madproject.proctorbook.R;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import static com.madproject.proctorbook.ProctorBook.getCurrentUserType;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetails extends Fragment {
    public PersonalDetails() {
    }

    ParseUser currentUser = ParseUser.getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }



    @Override
    public void onResume() {
        super.onResume();
        Button updatebtn = (Button)getView().findViewById(R.id.updateProfile);


        if (currentUser != null) {
            getActivity().setProgressBarIndeterminateVisibility(true);

            if (getCurrentUserType().equals("teacher")) {
                updatebtn.setVisibility(View.INVISIBLE);
                Intent oid = getActivity().getIntent();
                String oId = oid.getStringExtra("student_object_id");
                displayDetails(oId);

            } else if (getCurrentUserType().equals("student")) {
                displayDetails(currentUser.getObjectId());
            }
        }

    }

    public void displayDetails(String objectId) {

        final EditText first_name = (EditText) getView().findViewById(R.id.first_name);
        final EditText last_name = (EditText) getView().findViewById(R.id.last_name);
        final EditText email = (EditText) getView().findViewById(R.id.email);
        final EditText phoneno = (EditText) getView().findViewById(R.id.phoneno);
        final EditText address = (EditText) getView().findViewById(R.id.address);
        final Button updateProfilebtn = (Button)getView().findViewById(R.id.updateProfile);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("StudentDetails");
                    query1.whereEqualTo("user_id", object);

                    query1.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject parseObject, ParseException e) {

                            if (e == null) {
                                if (getCurrentUserType().equals("teacher")) {

                                    first_name.setFocusable(false);
                                    last_name.setFocusable(false);
                                    phoneno.setFocusable(false);
                                    address.setFocusable(false);
                                    email.setFocusable(false);

                                }

                                first_name.setText(parseObject.get("first_name").toString());
                                last_name.setText(parseObject.get("last_name").toString());
                                email.setText(object.get("email").toString());
                                phoneno.setText(parseObject.get("phone").toString());
                                address.setText(parseObject.get("address").toString());

                                updateProfilebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilder.setMessage("Are You Sure To Update Profile ?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String ufn = first_name.getText().toString();
                                                        String uln = last_name.getText().toString();
                                                        String uphno = phoneno.getText().toString();
                                                        String uadd = address.getText().toString();
                                                        String uemail = email.getText().toString();



                                                        if (ufn.isEmpty() || uln.isEmpty() || uphno.isEmpty() || uadd.isEmpty()) {
                                                            Toast.makeText(getActivity(), "Fill all fields correctly", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            parseObject.put("first_name", ufn);
                                                            parseObject.put("last_name", uln);
                                                            parseObject.put("phone", uphno);
                                                            parseObject.put("address", uadd);

                                                            parseObject.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    Toast.makeText(getActivity(), "Updated Profile Successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                            ParseUser u = ParseUser.getCurrentUser();
                                                            u.setEmail(uemail);
                                                            u.saveInBackground();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }


                        }
                    });
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
