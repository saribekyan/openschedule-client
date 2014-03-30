package edu.mit.openschedule.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject;
import edu.mit.openschedule.model.Subjects;
import edu.mit.openschedule.model.Subjects.MeetingType;
import edu.mit.openschedule.model.UserProfile;

public class SubjectFragment extends Fragment {
		
	public SubjectFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject, container,
				false);
		
		Bundle bundle = getArguments();
		Subject subject = Subjects.asList().get((Integer) bundle.get("subject_id"));
		
		ExpandableListView expListView = (ExpandableListView)
				rootView.findViewById(R.id.subject_exp_list_view);
		expListView.setAdapter(new SubjectAdapter(getActivity(), subject));
		
		Button addRemoveClass = (Button) rootView.findViewById(R.id.subject_add_remove_subject_button);
		if (UserProfile.getUserProfile().isTaking(subject)) {
			addRemoveClass.setText("Remove Class");
		} else {
			addRemoveClass.setText("Add Class");
		}
		
		return rootView;
	}

	private class SubjectAdapter extends BaseExpandableListAdapter {
		
		private final Context context;
		private final Subject subject;
		
		public SubjectAdapter(Context context, Subject subject) {
			this.context = context;
			this.subject = subject;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return subject;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout rowView = (LinearLayout)
					inflater.inflate(R.layout.subject_description_child_layout, parent, false);
			
			TextView descriptionTextView = (TextView) rowView.findViewById(R.id.subject_subject_description_text);
			descriptionTextView.setText(subject.getDescription());
			
			return rowView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition == 4 ? 0 : 1; // no children for rating
		}

		@Override
		public Object getGroup(int groupPosition) {
			return subject;
		}

		@Override
		public int getGroupCount() {
			return 5;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if (groupPosition == 0) {
				LinearLayout groupView = (LinearLayout)
					inflater.inflate(R.layout.subject_description_group_layout, parent, false);
				return groupView;
			} else if (groupPosition < 4) {
				LinearLayout groupView = (LinearLayout)
					inflater.inflate(R.layout.subject_meeting_times_group_layout, parent, false);
				
				MeetingType type = MeetingType.values()[groupPosition - 1];
				if (UserProfile.getUserProfile().isTaking(subject)) {
					((TextView) groupView.findViewById(R.id.subject_meeting_name_text)).setText(Subjects.MEETING_NAME.get(type) + ":");
					
					TextView timeLocationView = (TextView) groupView.findViewById(R.id.subject_meeting_time_text);
					Integer id = UserProfile.getUserProfile().getMeetingIdFor(subject, type);
					if (id == null) {
						timeLocationView.setText("N/A");
					} else if (id >= 0) {
						String timeString = subject.getMeeting(type, id).getTimeString();
						String locationString = subject.getMeeting(type, id).getLocationString();
						timeLocationView.setText(timeString + " (" + locationString + ")");
					} else if (id == -1) {
						timeLocationView.setText("Not set");
					} else {
						throw new IllegalStateException("oops");
					}
				} else {
					((TextView) groupView.findViewById(R.id.subject_meeting_name_text)).setText(Subjects.MEETING_NAME.get(type) + ":");
					((TextView) groupView.findViewById(R.id.subject_meeting_time_text)).setText("see times");;
				}
				return groupView;
			} else if (groupPosition == 4) {
				LinearLayout groupView = (LinearLayout)
						inflater.inflate(R.layout.subject_meeting_times_group_layout, parent, false);
					
				((TextView) groupView.findViewById(R.id.subject_meeting_name_text)).setText("Rating:");
				return groupView;
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return false;
		}
	}
}
