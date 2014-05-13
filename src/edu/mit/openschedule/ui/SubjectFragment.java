package edu.mit.openschedule.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject;
import edu.mit.openschedule.model.Subject.Meeting;
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
		Subject subject = Subjects.findByNumber((String)bundle.get(SubjectActivity.SUBJECT_NUMBER));
		
		((TextView) rootView.findViewById(R.id.subject_name_text)).setText(subject.getFullName());
		
		ExpandableListView expListView = (ExpandableListView)
				rootView.findViewById(R.id.subject_exp_list_view);
		expListView.setAdapter(new SubjectAdapter(getActivity(), subject, expListView));
		
		((RatingBar) rootView.findViewById(R.id.subject_rating_ratingbar)).setRating(subject.getRating());
		
//		Button addRemoveClass = (Button) rootView.findViewById(R.id.subject_add_remove_subject_button);
//		if (UserProfile.getUserProfile().isTaking(subject)) {
//			addRemoveClass.setText("Remove Class");
//		} else {
//			addRemoveClass.setText("Add Class");
//		}
		
		return rootView;
	}

	private class SubjectAdapter extends BaseExpandableListAdapter {
		
		private final Context context;
		private final Subject subject;
		private final ExpandableListView expListView;
		
		public SubjectAdapter(Context context, Subject subject, ExpandableListView expListView) {
			this.context = context;
			this.subject = subject;
			this.expListView = expListView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return subject;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			if (groupPosition == 0) {// Description
				LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				LinearLayout rowView = (LinearLayout)
						inflater.inflate(R.layout.subject_description_child_layout, parent, false);
				
				TextView descriptionTextView = (TextView) rowView.findViewById(R.id.subject_subject_description_text);
				descriptionTextView.setText(subject.getDescription());
				return rowView;
			}
			if (groupPosition <= 4) { // Lecture, Recitation, Lab
				final MeetingType type = MeetingType.values()[groupPosition - 1];
				final Meeting meeting = subject.getMeeting(type, childPosition);
				
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				LinearLayout childView = (LinearLayout)
						inflater.inflate(R.layout.subject_meeting_times_child_layout, parent, false);
				
				TextView timeLocText = (TextView) childView.findViewById(R.id.subject_meeting_child_time_loc_text);
				timeLocText.setText(meeting.getTimeString() + " (" + meeting.getLocationString() + ")");
				
				timeLocText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						UserProfile.getUserProfile().setMeeting(subject.getNumber(), type, childPosition);
						expListView.collapseGroup(groupPosition);
					}
				});
				
				return childView;
			}
			// Rating - none
			throw new RuntimeException("Invalid group position:" + groupPosition);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (groupPosition == 0) {
				return 1;
			}
			if (groupPosition < 4) {
				return subject.getMeetingCount(MeetingType.values()[groupPosition - 1]);
			}
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return subject;
		}

		@Override
		public int getGroupCount() {
			return 4;
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
			LinearLayout groupView;
			if (groupPosition == 0) { // Description
				groupView = (LinearLayout)
					inflater.inflate(R.layout.subject_description_group_layout, parent, false);
			} else if (groupPosition < 4) { // Lecture, Recitation, Lab
				groupView = (LinearLayout)
					inflater.inflate(R.layout.subject_meeting_times_group_layout, parent, false);
				
				MeetingType type = MeetingType.values()[groupPosition - 1];
				if (UserProfile.getUserProfile().isTaking(subject)) {
					((TextView) groupView.findViewById(R.id.subject_meeting_name_text)).setText(Subjects.MEETING_NAME.get(type) + ":");
					
					TextView timeLocationView = (TextView) groupView.findViewById(R.id.subject_meeting_group_time_loc_text);
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
					((TextView) groupView.findViewById(R.id.subject_meeting_group_time_loc_text)).setText("see times");;
				}
//			} else if (groupPosition == 4) {
//				groupView = (LinearLayout)
//						inflater.inflate(R.layout.subject_rating_group_layout, parent, false);
//					
//				RatingBar ratingBar = (RatingBar) groupView.findViewById(R.id.subject_rating_ratingbar);
//				ratingBar.setRating(subject.getRating());
			} else {
				throw new IllegalArgumentException();
			}
			return groupView;
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
