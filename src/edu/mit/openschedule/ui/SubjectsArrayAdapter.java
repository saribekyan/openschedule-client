package edu.mit.openschedule.ui;

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
import edu.mit.openschedule.model.Subject;

public class SubjectsArrayAdapter extends ArrayAdapter<Subject> {
	
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
		Subject subject = this.getItem(position);
		textView.setText(subject.getFullName());
		
		return rowView;
	}
	
	private List<Subject> getSubjectNamesContaining(String substr) {
		List<Subject> subjectsContaining = new ArrayList<Subject>();
		for (Subject subject : subjects) {
			if (subject.getName().contains(substr) ||
					subject.getNumber().startsWith(substr)) {
				subjectsContaining.add(subject);
			}
		}
		return subjectsContaining;
	}
}