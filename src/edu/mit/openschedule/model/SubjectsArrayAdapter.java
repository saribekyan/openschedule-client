package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.mit.openschedule.R;

public class SubjectsArrayAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final List<Subject> subjects;
	
	public SubjectsArrayAdapter(Context context, List<Subject> subjects) {
		super(context, R.layout.subject_name_layout);
		this.context = context;
		this.subjects = subjects;
		this.addAll(getSubjectNamesContaining(""));
	}
	
	public SubjectsArrayAdapter setSubstring(String substr) {
		this.clear();
		this.addAll(getSubjectNamesContaining(substr));
		return this;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout rowView = (LinearLayout)
				inflater.inflate(R.layout.subject_name_layout, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.subject_name_text);
		textView.setText(this.getItem(position));
		
		return rowView;
	}
	
	private List<String> getSubjectNamesContaining(String substr) {
		List<String> subjectsContaining = new ArrayList<String>();
		for (Subject subject : subjects) {
			if (subject.getSubjectName().contains(substr) ||
					subject.getSubjectNumber().startsWith(substr)) {
				subjectsContaining.add(subject.getSubjectNumber() + " " + subject.getSubjectName());
			}
		}
		return subjectsContaining;
	}
}