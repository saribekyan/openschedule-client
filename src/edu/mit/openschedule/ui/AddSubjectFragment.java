package edu.mit.openschedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject;
import edu.mit.openschedule.model.Subjects;

public class AddSubjectFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_subject, container,
				false);
		
		final ListView listView = (ListView) rootView.findViewById(R.id.subjects_suggested_subject_list);
		final SubjectsArrayAdapter adapter = new SubjectsArrayAdapter(getActivity(), Subjects.asList());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Subject subject = adapter.getItem(position);
				Intent intent = new Intent(getActivity(), SubjectActivity.class);
				intent.putExtra(SubjectActivity.SUBJECT_ID, subject.getId());
				startActivity(intent);
			}
		});
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
