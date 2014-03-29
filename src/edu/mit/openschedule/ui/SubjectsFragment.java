package edu.mit.openschedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.SubjectsArrayAdapter;
import edu.mit.openschedule.model.UserProfile;

public class SubjectsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subjects, container,
				false);
		
		Button addClassButton = (Button) rootView.findViewById(R.id.subjects_add_class_button);
		addClassButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddSubjectActivity.class));
			}
		});
		
		ListView listView = (ListView) rootView.findViewById(R.id.subjetcs_subject_list);
		SubjectsArrayAdapter adapter = new SubjectsArrayAdapter(
				getActivity(), UserProfile.getUserProfile().getSubjectList());
		listView.setAdapter(adapter);
		
		return rootView;
	}
}
