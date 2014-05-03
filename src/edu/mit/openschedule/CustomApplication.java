package edu.mit.openschedule;


import android.app.Application;

import com.parse.Parse;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "roVXtrENMwnxSkc1wQaFZrDRZMMNp1tbi9pFpTeq", "okIPa9Q0b1PpSnHsTfiGu8aFnoTe1Vp5O7KZ1nxb");
    }
}
