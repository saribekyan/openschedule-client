package edu.mit.openschedule;


import java.util.List;

import android.app.Application;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "roVXtrENMwnxSkc1wQaFZrDRZMMNp1tbi9pFpTeq", "okIPa9Q0b1PpSnHsTfiGu8aFnoTe1Vp5O7KZ1nxb");
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", "[admin]"); 
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() == 1) {
                    ParseUser adminUser = userList.get(0);
                    ParseACL defaultACL = new ParseACL();
                    defaultACL.setReadAccess(adminUser, true);
                    defaultACL.setWriteAccess(adminUser, true);
                    ParseACL.setDefaultACL(defaultACL, false);
                }
            }
        });
    }
}
