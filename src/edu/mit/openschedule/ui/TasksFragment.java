package edu.mit.openschedule.ui;

import java.util.List;

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
import edu.mit.openschedule.model.Task;
import edu.mit.openschedule.model.Task.Status;
import edu.mit.openschedule.model.UserProfile;

public class TasksFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
		
		final ExpandableListView listView =
				(ExpandableListView) rootView.findViewById(R.id.tasks_exp_list);
		
		TasksAdapter adapter = new TasksAdapter(getActivity());
		listView.setAdapter(adapter);
		
		return rootView;
	}
	
	private class TasksAdapter extends BaseExpandableListAdapter {
		
		private Context context;
		private List<Task> tasks;
		
		public TasksAdapter(Context context) {
			this.context = context;
			this.tasks = UserProfile.getUserProfile().getNotSubmittedTasksSorted();
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return tasks.get(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			Task task = tasks.get(groupPosition);
			
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout rowView = (LinearLayout)
					inflater.inflate(R.layout.task_view_layout, parent, false);
			
			LinearLayout personalDeadline = (LinearLayout)
					rowView.findViewById(R.id.tasks_personal_deadline_layout);
			
			TextView finishedText =
					(TextView) personalDeadline.findViewById(R.id.tasks_finished_text);
			Button personalDeadlineButton =
					(Button) personalDeadline.findViewById(R.id.tasks_button_pick_personal_date_time);
			if (task.getStatus() == Status.UNFINISHED) {
				personalDeadlineButton.setText(task.getPersonalDeadlineString());
				personalDeadlineButton.setVisibility(View.VISIBLE);
				finishedText.setVisibility(View.GONE);
				personalDeadlineButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
					}
				});
			} else {
				personalDeadlineButton.setVisibility(View.GONE);
				finishedText.setVisibility(View.VISIBLE);
			}
			
			LinearLayout classDeadline = (LinearLayout)
					rowView.findViewById(R.id.tasks_class_deadline_layout);
			Button classDeadlineButton =
					(Button) classDeadline.findViewById(R.id.tasks_button_pick_class_date_time);
			classDeadlineButton.setText(task.getClassDeadlineString());
			classDeadlineButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});

			LinearLayout othersSpent = (LinearLayout)
					rowView.findViewById(R.id.tasks_others_spent_layout);
			
			TextView othersSpentText =
					(TextView) othersSpent.findViewById(R.id.tasks_others_spent_value);
			othersSpentText.setText(task.getOthersSpentString());
			if (task.getStatus() != Status.UNFINISHED) {
				LinearLayout userSpent = (LinearLayout)
						rowView.findViewById(R.id.tasks_user_spent_layout);
				TextView userSpentText =
						(TextView) userSpent.findViewById(R.id.tasks_user_spent_value);
				userSpentText.setText(task.getUserSpentString());
				
				rowView.findViewById(R.id.tasks_user_spent_layout).setVisibility(View.VISIBLE);			
			} else {
				rowView.findViewById(R.id.tasks_user_spent_layout).setVisibility(View.GONE);
			}
			
			LinearLayout submitLocation = (LinearLayout)
					rowView.findViewById(R.id.tasks_submit_location_layout);
			TextView submitLocationText =
					(TextView) submitLocation.findViewById(R.id.tasks_submit_location_value);
			submitLocationText.setText(task.getSubmitLocation());
			
			if (task.getStatus() == Status.UNFINISHED) {
				Button finishSubmitButton = (Button) rowView.findViewById(R.id.tasks_finish_submit_button);
				finishSubmitButton.setText("Just Finished!");
				finishSubmitButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
					}
				});
			} else {
				Button finishSubmitButton = (Button) rowView.findViewById(R.id.tasks_finish_submit_button);
				finishSubmitButton.setText("Just Submitted!");
			}
			
			return rowView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return tasks.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return tasks.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			Task task = tasks.get(groupPosition);
			
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout groupView = (LinearLayout)
					inflater.inflate(R.layout.tasks_row_group, parent, false);
			
			TextView taskNameView = (TextView) groupView.findViewById(R.id.tasks_task_name_text);
			taskNameView.setText(task.getName());
			
			TextView personalDeadlineView = (TextView) groupView.findViewById(R.id.tasks_task_deadline_text);
			personalDeadlineView.setText(task.getPersonalDeadlineString());
			return groupView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
}
