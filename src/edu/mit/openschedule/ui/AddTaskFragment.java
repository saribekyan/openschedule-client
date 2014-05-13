package edu.mit.openschedule.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Task;
import edu.mit.openschedule.model.UserProfile;

public class AddTaskFragment extends Fragment {
	
	
	private static final Integer[] NUMBERS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
		23, 24, 25, 26, 27, 28, 29, 30
	};
	
	private final Calendar deadlineCalendar = new GregorianCalendar();
	private Button dateTimeButton;
	private Bundle mSavedInstanceState;
	
	public AddTaskFragment() {
		super();
		deadlineCalendar.clear();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSavedInstanceState = savedInstanceState;
		if (mSavedInstanceState != null && mSavedInstanceState.containsKey("deadline")) {
			deadlineCalendar.setTimeInMillis(mSavedInstanceState.getLong("deadline"));
		}
		View rootView = inflater.inflate(R.layout.fragment_add_task, container,
				false);
		
		final Spinner selectSubject = (Spinner) rootView.findViewById(R.id.add_task_select_subject_dropdown);
		List<String> subjectsString = UserProfile.getUserProfile().getSubjectsString();
		ArrayAdapter<String> selectSubjectAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_simple_text, subjectsString);
		selectSubject.setAdapter(selectSubjectAdapter);
		
		final Spinner selectAssignment = (Spinner) rootView.findViewById(R.id.add_task_select_assignment_dropdown);
		ArrayAdapter<String> selectAssignmentAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_simple_text, Task.TASKS);
		selectAssignment.setAdapter(selectAssignmentAdapter);
		
		final Spinner selectAssignmentNumber = (Spinner) rootView.findViewById(R.id.add_task_select_number_spinner);
		ArrayAdapter<Integer> selectAssignmentNumberAdapter = new ArrayAdapter<Integer>(
				getActivity(), R.layout.spinner_simple_text, NUMBERS);
		selectAssignmentNumber.setAdapter(selectAssignmentNumberAdapter);
		
		dateTimeButton = (Button) rootView.findViewById(R.id.add_task_button_pick_class_date_time);
		if (!deadlineCalendar.isSet(Calendar.YEAR)) {
			dateTimeButton.setText("Click to enter");
		} else {
			dateTimeButton.setText(new SimpleDateFormat("MM/dd@hh:mmaa", Locale.getDefault()).format(deadlineCalendar.getTime()));
		}
		dateTimeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DateTimePickerActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		final EditText locationEditText = (EditText) rootView.findViewById(R.id.add_task_submit_location_edittext);
		
		Button doneButton = (Button) rootView.findViewById(R.id.add_task_done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserProfile profile = UserProfile.getUserProfile();
				if (!deadlineCalendar.isSet(Calendar.YEAR) ) {
					Toast.makeText(getActivity(), "Please enter the deadline", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (!deadlineCalendar.after(Calendar.getInstance())) {
					Toast.makeText(getActivity(), "Please enter a valid daedline.", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (locationEditText.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "Please enter submit location", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (!profile.addTask(
						selectSubject.getSelectedItemPosition(),
						selectAssignment.getSelectedItemPosition(),
						selectAssignmentNumber.getSelectedItemPosition(),
						deadlineCalendar,
						locationEditText.getText().toString())) {
					Toast.makeText(getActivity(), "Task already exists", Toast.LENGTH_LONG).show();
					return;
				}
				
				getActivity().finish();
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent res) {
		super.onActivityResult(requestCode, resultCode, res);
		
		if (resultCode < 0) {
			return;
		}
		Calendar now = Calendar.getInstance();
		deadlineCalendar.set(Calendar.YEAR, res.getIntExtra("year", now.get(Calendar.YEAR)));
		deadlineCalendar.set(Calendar.MONTH, res.getIntExtra("month", now.get(Calendar.MONTH)));
		deadlineCalendar.set(Calendar.DAY_OF_MONTH, res.getIntExtra("day", now.get(Calendar.DAY_OF_MONTH)));
		deadlineCalendar.set(Calendar.HOUR_OF_DAY, res.getIntExtra("hour", now.get(Calendar.HOUR_OF_DAY)));
		deadlineCalendar.set(Calendar.MINUTE, res.getIntExtra("minute", now.get(Calendar.MINUTE)));
		
		dateTimeButton.setText(new SimpleDateFormat("MM/dd@hh:mmaa", Locale.getDefault()).format(deadlineCalendar.getTime()));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("deadline", deadlineCalendar.getTimeInMillis());
	}
}
