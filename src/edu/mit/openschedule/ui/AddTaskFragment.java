package edu.mit.openschedule.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.UserProfile;

public class AddTaskFragment extends Fragment {
	
	private static final String[] ASSIGNMENTS = {"Problem Set", "Project", "Paper", "Lab", "Hands on"}; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_task, container,
				false);
		
		Spinner selectSubject = (Spinner) rootView.findViewById(R.id.add_task_select_subject_dropdown);
		List<String> subjectsString = UserProfile.getUserProfile().getSubjectsString();
		ArrayAdapter<String> selectSubjectAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_simple_text, subjectsString);
		selectSubject.setAdapter(selectSubjectAdapter);
		
		Spinner selectAssignment = (Spinner) rootView.findViewById(R.id.add_task_select_assignment_dropdown);
		ArrayAdapter<String> selectAssignmentAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_simple_text, ASSIGNMENTS);
		selectAssignment.setAdapter(selectAssignmentAdapter);
		
		NumberPicker selectNumber = (NumberPicker) rootView.findViewById(R.id.add_task_select_number_picker);
		selectNumber.setMaxValue(20);
		selectNumber.setMinValue(0);

		return rootView;
	}
}
