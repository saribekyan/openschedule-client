package edu.mit.openschedule.ui;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import edu.mit.openschedule.R;
import edu.mit.openschedule.model.LocalUserProfile;
import edu.mit.openschedule.model.ParseServer;
import edu.mit.openschedule.model.Subjects;
import edu.mit.openschedule.model.UserProfile;
import edu.mit.openschedule.ui.InfoDialogFragment.InfoDialogFragmentListener;

public class HomeActivity extends ActionBarActivity implements
		ActionBar.TabListener, InfoDialogFragmentListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getSupportActionBar().setTitle(R.string.app_name);
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Subjects.addSubjects(ParseServer.loadSubjectList());
        List<String> userSubjectNumbers = ParseServer.getUserSubjectNumbers();
        UserProfile.getUserProfile().setSubjects(userSubjectNumbers);
        LocalUserProfile.loadUserProfile();
        UserProfile.getUserProfile().setTasks(ParseServer.loadTaskList(userSubjectNumbers), false);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_logout) {
		    ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment[] fragments = new Fragment[3]; 
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (fragments[position] != null) {
				return fragments[position];
			}
			switch (position) {
			case 0:
				return fragments[position] = new CalendarFragment();
			case 1:
				return fragments[position] = new TasksFragment();
			case 2:
				return fragments[position] = new SubjectsFragment();
			}
			throw new IllegalArgumentException(String.format("position = %d", position));
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.home_title_calendar).toUpperCase(l);
			case 1:
				return getString(R.string.home_title_tasks).toUpperCase(l);
			case 2:
				return getString(R.string.home_title_classes).toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
	public void onDialogPositiveClick(InfoDialogFragment dialog, String text) {
		if (text == null || text.equals(""))
			return;
		InfoDialogFragment my = (InfoDialogFragment) dialog;
		UserProfile profile = UserProfile.getUserProfile();
		if (my.getDialogType() == 0) {
			profile.getTask(my.getTaskName()).setSubmitLocation(text);
			// Send the changes to the server
			UserProfile.getUserProfile().addTask(profile.getTask(my.getTaskName()), true);
		} else {
			try {
				profile.getTask(my.getTaskName()).finish(Double.parseDouble(text));
				profile.addTask(profile.getTask(my.getTaskName()), true);
			} catch (NumberFormatException e){
				Toast.makeText(this, R.string.time_spent_is_number, Toast.LENGTH_LONG).show();
			}
		}
		((TasksFragment)mSectionsPagerAdapter.getItem(1)).refresh();
	}

	@Override
	public void onDialogNegativeClick(InfoDialogFragment dialog) {
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		((CalendarFragment)mSectionsPagerAdapter.getItem(0)).refresh();
		((TasksFragment)mSectionsPagerAdapter.getItem(1)).refresh();
	}
}
