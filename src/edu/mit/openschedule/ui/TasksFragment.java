package edu.mit.openschedule.ui;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import edu.mit.openschedule.model.ParseServer;
import edu.mit.openschedule.model.Task;
import edu.mit.openschedule.model.Task.Status;
import edu.mit.openschedule.model.UserProfile;

public class TasksFragment extends Fragment {
	
	private TasksAdapter mTasksAdapter;
	private Thread updateTasks; // This thread refreshes task list in every REFRESH_INTERVAL milliseconds
	private static final int REFRESH_INTERVAL = 1000;
	private ExpandableListView mExpListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
		
		Button addTaskButton = (Button) rootView.findViewById(R.id.tasks_add_task_button);
		addTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddTaskActivity.class);
				startActivity(intent);
			}
		});
		
		mExpListView =
				(ExpandableListView) rootView.findViewById(R.id.tasks_exp_list);
		
		mTasksAdapter = new TasksAdapter(getActivity());
		mExpListView.setAdapter(mTasksAdapter);
		
		updateTasks = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        List<String> subjectNumbers = UserProfile.getUserProfile().getSubjectNumbers();
                        List<Task> tasks = ParseServer.loadTaskList(subjectNumbers);
                        refreshFromBackground(tasks);
                        Thread.sleep(REFRESH_INTERVAL);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
		updateTasks.start();
		
		return rootView;
	}
	
	@Override
	public void onDestroy() {
	    updateTasks.interrupt();
	    try {
            updateTasks.join();
        } catch (InterruptedException e) { }
	    super.onDestroy();
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
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final Task task = tasks.get(groupPosition);
			
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout rowView = (LinearLayout)
					inflater.inflate(R.layout.task_view_layout, parent, false);
			
			LinearLayout personalDeadline = (LinearLayout)
					rowView.findViewById(R.id.tasks_personal_deadline_layout);
			
			TextView finishedText =
					(TextView) personalDeadline.findViewById(R.id.tasks_finished_text);
			final Button personalDeadlineButton =
					(Button) personalDeadline.findViewById(R.id.tasks_button_pick_personal_date_time);
			if (task.getStatus() == Status.UNFINISHED) {
				personalDeadlineButton.setText(task.getPersonalDeadlineString());
				personalDeadlineButton.setVisibility(View.VISIBLE);
				finishedText.setVisibility(View.GONE);
				personalDeadlineButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), DateTimePickerActivity.class);
						intent.putExtra("task_name", task.getName());
						startActivityForResult(intent, 0);
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
					Intent intent = new Intent(getActivity(), DateTimePickerActivity.class);
					intent.putExtra("task_name", task.getName());
					startActivityForResult(intent, 1);
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
			Button submitLocationButton =
					(Button) submitLocation.findViewById(R.id.tasks_submit_location_button);
			submitLocationButton.setText(task.getSubmitLocation());
			submitLocationButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DialogFragment dialog = new InfoDialogFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("dialog_type", 0);
					bundle.putString("task_name", task.getName());
					dialog.setArguments(bundle);
					dialog.show(getActivity().getSupportFragmentManager(), "LocationDialog");
				}
			});
			
			if (task.getStatus() == Status.UNFINISHED) {
				Button finishSubmitButton = (Button) rowView.findViewById(R.id.tasks_finish_submit_button);
				finishSubmitButton.setText("Just Finished!");
				finishSubmitButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DialogFragment dialog = new InfoDialogFragment();
						Bundle bundle = new Bundle();
						bundle.putInt("dialog_type", 1);
						bundle.putString("task_name", task.getName());
						dialog.setArguments(bundle);
						dialog.show(getActivity().getSupportFragmentManager(), "LocationDialog");						
					}
				});
			} else {
				Button finishSubmitButton = (Button) rowView.findViewById(R.id.tasks_finish_submit_button);
				finishSubmitButton.setText("Just Submitted!");
				finishSubmitButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						UserProfile.getUserProfile().getTask(task.getName()).submit();
						TasksFragment.this.refresh();
					}
				});
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
					inflater.inflate(R.layout.tasks_row_group_layout, parent, false);
			
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

		public void setTasks(List<Task> notSubmittedTasksSorted) {
			this.tasks = notSubmittedTasksSorted;
		}
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent res) {
		super.onActivityResult(reqCode, resultCode, res);
		if (resultCode < 0) {
			return;
		}
		UserProfile profile = UserProfile.getUserProfile();
		
		if (reqCode <= 1) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, res.getIntExtra("year", cal.get(Calendar.YEAR)));
			cal.set(Calendar.MONTH, res.getIntExtra("month", cal.get(Calendar.MONTH)));
			cal.set(Calendar.DAY_OF_MONTH, res.getIntExtra("day", cal.get(Calendar.DAY_OF_MONTH)));
			cal.set(Calendar.HOUR, res.getIntExtra("hour", cal.get(Calendar.HOUR_OF_DAY)));
			cal.set(Calendar.MINUTE, res.getIntExtra("minute", cal.get(Calendar.MINUTE)));
			String taskName = res.getStringExtra("task_name");
			if (reqCode == 0) {
				profile.getTask(taskName).setPersonalDeadline(cal);
			} else if (reqCode == 1) {
				profile.getTask(taskName).changeClassDeadline(cal);
				profile.addTask(profile.getTask(taskName), true);
			}
		} else {
			
		}
	}
	
	public void refresh() {
		if (mTasksAdapter != null) {
			mTasksAdapter.setTasks(UserProfile.getUserProfile().getNotSubmittedTasksSorted());
			mTasksAdapter.notifyDataSetChanged();
		}
	}
	
	public void refreshFromBackground(final List<Task> tasks) {
        TasksFragment.this.getActivity().runOnUiThread(
    	    new Runnable() {
                @Override
                public void run() {
                    UserProfile.getUserProfile().setTasks(tasks, false);
                    TasksFragment.this.refresh();
                }
            });
	}
}
