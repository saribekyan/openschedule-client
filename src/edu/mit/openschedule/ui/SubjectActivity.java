package edu.mit.openschedule.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject;
import edu.mit.openschedule.model.Subjects;
import edu.mit.openschedule.model.UserProfile;

public class SubjectActivity extends ActionBarActivity {
	
	public static final String SUBJECT_NUMBER = "subject_number";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
		setContentView(R.layout.activity_subject);

		if (savedInstanceState == null) {
			///////////////////////////// test
			UserProfile.getUserProfile();
			
			String number = getIntent().getStringExtra(SUBJECT_NUMBER);
			
			Bundle bundle = new Bundle();
			bundle.putString(SUBJECT_NUMBER, number);
			
			SubjectFragment subjectFragment = new SubjectFragment();
			subjectFragment.setArguments(bundle);
			////////////////////////////////////
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, subjectFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subject, menu);
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
}
