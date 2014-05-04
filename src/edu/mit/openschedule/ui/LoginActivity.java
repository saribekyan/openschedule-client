package edu.mit.openschedule.ui;

import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.mit.openschedule.R;
import edu.mit.openschedule.model.ParseServer;
import edu.mit.openschedule.model.UserProfile;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** A placeholder fragment containing a simple view. */
    public static class PlaceholderFragment extends Fragment {
        
        private EditText mUserName;
        private EditText mPassword;
        private Button mLogin;
        private ProgressDialog mProgress;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            mUserName = (EditText)rootView.findViewById(R.id.login_edittext_username);
            mPassword = (EditText)rootView.findViewById(R.id.login_edittext_password);
            mLogin = (Button)rootView.findViewById(R.id.login_button_login);
            mLogin.setOnClickListener(new LoginButtonOnClickListener());
            
            // By default, users don't have access to anything they create
            ParseACL.setDefaultACL(new ParseACL(), false);
            return rootView;
        }
        
        private void loginSuccessful() {
            mUserName.setEnabled(true);
            mPassword.setEnabled(true);
            mLogin.setEnabled(true);
            mProgress.dismiss();
            List<String> ss = ParseServer.getSubjectNumbers();
            for (String s : ss) {
                Log.d("TAG", s);
            }
//            List<Subject> ls = ParseServer.getSubjects(ss);
//            for (Subject s : ls) {
//                Subjects.addSubject(s);
//            }
            UserProfile.getUserProfile();
            Intent intent = new Intent(PlaceholderFragment.this.getActivity(), HomeActivity.class);
            startActivity(intent);
        }
        
        private void loginUnsuccessful() {
            // If some kind of other error happens (i.e. Network connection drops)
            // then cancel the progress dialog and return to the starting position
            mProgress.dismiss();
            mUserName.setEnabled(true);
            mPassword.setEnabled(true);
            mLogin.setEnabled(true);
            Toast.makeText(getActivity(), R.string.login_toast_unable, Toast.LENGTH_SHORT).show();
        }

        private class LogInUser extends LogInCallback {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    // If the user already exists, just log in
                    loginSuccessful();
                } else if (e.getCode() == 101) {
                    // If such user doesn't exist, create new user
                    // (by putting in Pending table, where adminUser will take care of it)
                    mProgress.setMessage(getString(R.string.login_progress_check_mit));
                    ParseObject object = new ParseObject("Pending");
                    final String username = mUserName.getText().toString().toLowerCase(Locale.US);
                    final String password = mPassword.getText().toString();
                    object.put("username", username);
                    object.put("password", password);
                    object.saveInBackground();
                    new Thread(new Runnable() {
                        private static final int TRYOUTS = 5;
                        private static final int INTERVAL = 2000; // in milliseconds
                        @Override
                        public void run() {
                            for (int i = 0; i < TRYOUTS; i++) {
                                long t1 = System.currentTimeMillis();
                                try {
                                    ParseUser.logIn(username, password);
                                    // run loginSuccessful() on UI thread
                                    PlaceholderFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loginSuccessful();
                                        }
                                    });
                                    return;
                                } catch (ParseException e1) { } // Ignore
                                long t2 = System.currentTimeMillis();
                                if (t2-t1 < INTERVAL) {
                                    try {
                                        Thread.sleep(INTERVAL - t2 + t1);
                                    } catch (InterruptedException e) {
                                        break;
                                    }
                                }
                            }
                            // After TRYOUTS tries, return unsuccessful login
                            PlaceholderFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loginUnsuccessful();
                                }
                            });

                        }
                    }).start();
                } else {
                    loginUnsuccessful();
                }
            }
        }
        
        private class LoginButtonOnClickListener implements OnClickListener {
            @Override
            public void onClick(View v) {
                mUserName.setEnabled(false);
                mPassword.setEnabled(false);
                mLogin.setEnabled(false);
                mProgress = new ProgressDialog(PlaceholderFragment.this.getActivity());
                mProgress.setTitle(getString(R.string.login_progress_title));
                mProgress.setMessage(getString(R.string.login_progress_logging_in));
                mProgress.show();
                mProgress.setCancelable(false);
                ParseUser.logInInBackground(mUserName.getText().toString().toLowerCase(Locale.US),
                        mPassword.getText().toString(),
                        new LogInUser());
            }
        }
        
        
    }
}
