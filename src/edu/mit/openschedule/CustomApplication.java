package edu.mit.openschedule;


import android.app.Application;
import android.content.Context;

import com.parse.Parse;

import edu.mit.openschedule.model.UserProfile;

public class CustomApplication extends Application {

    public static Context context;
    
    @Override
    public void onCreate() {
        Parse.initialize(this, "roVXtrENMwnxSkc1wQaFZrDRZMMNp1tbi9pFpTeq", "okIPa9Q0b1PpSnHsTfiGu8aFnoTe1Vp5O7KZ1nxb");
        UserProfile.getUserProfile();
        context = getApplicationContext();
    }
}
