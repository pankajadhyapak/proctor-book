package com.madproject.proctorbook;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

public class ProctorBook extends Application {

    public void onCreate() {

        /*
            Initialize parse library before doing anything !!
         */
        Parse.initialize(
                this,
                "90du965bbcbmsRDqTVMDleW49ZSi6GhrVpztEHNk",
                "kxgxPb9wcfU1sSHwnp7SlX6IrlQGozJiJmOsCeoL"
        );
    }

    /*
        @return string
        Determine the type of Currently logged in User
     */
    public static String getCurrentUserType(){

      ParseUser currentUser = ParseUser.getCurrentUser();

      if(currentUser != null) return currentUser.get("type").toString();

      return "none";
    }

}
