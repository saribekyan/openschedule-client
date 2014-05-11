package edu.mit.openschedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject;
import edu.mit.openschedule.model.UserProfile;

public class SubjectsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subjects, container, false);
		
		Button addClassButton = (Button) rootView.findViewById(R.id.subjects_add_subject_button);
		addClassButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddSubjectActivity.class));
			}
		});
		
		ListView listView = (ListView) rootView.findViewById(R.id.subjects_subject_list);
		final SubjectsArrayAdapter adapter = new SubjectsArrayAdapter(
				getActivity(), UserProfile.getUserProfile().getSubjectList());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Subject subject = adapter.getItem(position);
				Intent intent = new Intent(getActivity(), SubjectActivity.class);
				intent.putExtra(SubjectActivity.SUBJECT_NUMBER, subject.getNumber());
				startActivity(intent);
			}
		});
		
		return rootView;
	}
}
