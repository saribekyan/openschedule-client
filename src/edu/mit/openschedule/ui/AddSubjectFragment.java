package edu.mit.openschedule.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subjects;
import edu.mit.openschedule.model.SubjectsArrayAdapter;

public class AddSubjectFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_subject, container,
				false);
		
		ListView listView = (ListView) rootView.findViewById(R.id.subjects_suggested_subject_list);
		SubjectsArrayAdapter adapter = new SubjectsArrayAdapter(getActivity(), Subjects.asList());
		listView.setAdapter(adapter);
		
		EditText editText = (EditText) rootView.findViewById(R.id.subjects_enter_subject);
		editText.addTextChangedListener(new EnterClassTextWatcher(adapter));
		
		return rootView;
	}
	

	
	private class EnterClassTextWatcher implements TextWatcher {
		
		private final SubjectsArrayAdapter adapter;
		public EnterClassTextWatcher(SubjectsArrayAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			adapter.setSubstring(s.toString()).notifyDataSetChanged();
		}
	}
}
