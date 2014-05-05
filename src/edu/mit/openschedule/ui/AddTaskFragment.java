package edu.mit.openschedule.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.UserProfile;

public class AddTaskFragment extends Fragment {
	
	private static final String[] ASSIGNMENTS = {"Problem Set", "Project", "Paper", "Lab", "Hands on"};
	private static final Integer[] NUMBERS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
		23, 24, 25, 26, 27, 28, 29, 30
	};
	
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
		
		Spinner selectAssignmentNumber = (Spinner) rootView.findViewById(R.id.add_task_select_number_spinner);
		ArrayAdapter<Integer> selectAssignmentNumberAdapter = new ArrayAdapter<Integer>(
				getActivity(), R.layout.spinner_simple_text, NUMBERS);
		selectAssignmentNumber.setAdapter(selectAssignmentNumberAdapter);

		return rootView;
	}
}
