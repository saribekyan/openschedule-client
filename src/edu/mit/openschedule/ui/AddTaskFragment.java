package edu.mit.openschedule.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
	
	public static int year = -1;
	public static int month = -1;
	public static int day = -1;
	public static int hour = -1;
	public static int minute = -1;
	public static Button selectTimeButton;
	public static Button selectDateButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		
		selectTimeButton = (Button) rootView.findViewById(R.id.add_task_pick_class_time_button);
		selectTimeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment timePickerFragment = new TimePickerFragment();
				
				Bundle bundle = new Bundle();
				bundle.putInt("task_id", -1);
			    timePickerFragment.setArguments(bundle);
			    
			    timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});
		
		selectDateButton = (Button) rootView.findViewById(R.id.add_task_pick_class_date_button);
		selectDateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment datePickerFragment = new DatePickerFragment();
				
				Bundle bundle = new Bundle();
				bundle.putInt("task_id", -1);
				datePickerFragment.setArguments(bundle);
			    
				datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
			}
		});
		
		if (year != -1) {
			selectDateButton.setText(month + "/" + day);
		}
		if (hour != -1) {
			selectTimeButton.setText(hour + "/" + minute);
		}
		
		final EditText locationEditText = (EditText) rootView.findViewById(R.id.add_task_submit_location_edittext);
		
		Button doneButton = (Button) rootView.findViewById(R.id.add_task_done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserProfile profile = UserProfile.getUserProfile();
				if (!profile.addTask(
						selectSubject.getSelectedItemPosition(),
						selectAssignment.getSelectedItemPosition(),
						selectAssignmentNumber.getSelectedItemPosition(),
						year, month, day, hour, minute,
						locationEditText.getText().toString())) {
					Toast.makeText(getActivity(), "Task already exists", Toast.LENGTH_LONG).show();
					return;
				}
				Fragment fragment = new TasksFragment();

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.container, fragment);
				transaction.commit();
			}
		});
		
		return rootView;
	}
}
