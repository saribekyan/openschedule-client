package edu.mit.openschedule.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Time;
import edu.mit.openschedule.model.UserProfile;
import edu.mit.openschedule.model.WeekdayTime;

public class CalendarFragment extends Fragment {
	
	private Calendar calendar;
	private CalendarAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		
		LinearLayout topLayout = (LinearLayout) rootView.findViewById(R.id.calendar_top_row);
		TextView date = (TextView) topLayout.findViewById(R.id.calendar_date);
		
		calendar = Calendar.getInstance();
		date.setText(
					new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(calendar.getTime()));
		
		final ListView listView = (ListView) rootView.findViewById(R.id.calendar_list);
		
		adapter = new CalendarAdapter(getActivity());
		listView.setAdapter(adapter);
		
		setChangeDayEventHandler(topLayout.findViewById(R.id.calendar_next_day_button), date, +1, adapter);
		setChangeDayEventHandler(topLayout.findViewById(R.id.calendar_previous_day_button), date, -1, adapter);
		
		return rootView;
	}

	private void setChangeDayEventHandler(View button, final TextView date, final int deltaDay, final CalendarAdapter adapter) {
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				calendar.add(Calendar.DATE, deltaDay);
				date.setText(
						new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(calendar.getTime()));
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private class CalendarAdapter extends ArrayAdapter<Time> {
				
		private final Context context;
		private final List<Time> times;
		
		public CalendarAdapter(Context context) {
			super(context, R.layout.calendar_row_layout);
			this.context = context;
			times = new ArrayList<Time>();
			updateTimes();
			this.addAll(times);
		}
		
		int colorIndex = 0;
		private final Integer[] colors = {
            0xff33B5E5,
            0xffAA66CC,
            0xff99CC00,
            0xffFFBB33,
            0xffFF4444,
            0xff0099CC,
            0xff9933CC,
            0xff669900,
            0xffFF8800,
            0xffCC0000,
		};
		private final Map<Meeting, Integer> meetingNamePosition =
				new HashMap<Meeting, Integer>();
		private final Map<Meeting, Integer> meetingColor =
				new HashMap<Meeting, Integer>();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout rowView = (LinearLayout)
					inflater.inflate(R.layout.calendar_row_layout, parent, false);
			
			if (position % 2 == 0) { // round hours
				TextView textView = (TextView) rowView.findViewById(R.id.text_calendar_time);
				textView.setText(times.get(position).toString());
			}
			
			UserProfile profile = UserProfile.getUserProfile();
			
			WeekdayTime wt = new WeekdayTime(getWeekDayChar(), times.get(position));
			
			List<Meeting> meetings = profile.meetingsAt(wt);
			for (final Meeting meeting : meetings) {
				Button button = new Button(context);
				button.setGravity(Gravity.TOP);
				if (!meetingNamePosition.containsKey(meeting) ||
						meetingNamePosition.get(meeting) == position) {
					button.setText(meeting.getSubject().getNumber() + " " + meeting.getTypeString());
					meetingNamePosition.put(meeting, position);
				}
				if (!meetingColor.containsKey(meeting)) {
					meetingColor.put(meeting, colors[colorIndex]);
					colorIndex = (colorIndex + 1) % colors.length;
				}
				button.setBackgroundColor(meetingColor.get(meeting));
			    LinearLayout.LayoutParams buttonParams =
			    		new LinearLayout.LayoutParams(
			    				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
			    rowView.addView(button, buttonParams);
			    
			    button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), SubjectActivity.class);
						intent.putExtra(SubjectActivity.SUBJECT_NUMBER, meeting.getSubject().getNumber());
						startActivity(intent);
					}
				});
			}

		    return rowView;
		}
		
		private char getWeekDayChar() {
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				return 'S';
			case Calendar.MONDAY:
				return 'M';
			case Calendar.TUESDAY:
				return 'T';
			case Calendar.WEDNESDAY:
				return 'W';
			case Calendar.THURSDAY:
				return 'R';
			case Calendar.FRIDAY:
				return 'F';
			case Calendar.SATURDAY:
				return 'S';
			default:
				throw new IllegalStateException();
			}
		}

		public void updateTimes() {
			times.clear();
			Time current = Time.START;
			while (current.before(Time.END)) {
				times.add(current);
				current = current.timeAfter(30);
			}
		}
	}

	public void refresh() {
		if (adapter != null) {
			adapter.updateTimes();
			adapter.notifyDataSetChanged();
		}
	}
}
