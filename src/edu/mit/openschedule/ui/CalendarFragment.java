package edu.mit.openschedule.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
	
	private char dayOfWeekChar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		
		TextView date = (TextView) rootView.findViewById(R.id.calendar_date);
		
		Calendar calendar = Calendar.getInstance();
		date.setText(
					new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(calendar.getTime()));
		dayOfWeekChar = getWeekDayChar(calendar);
		
		final ListView listView = (ListView) rootView.findViewById(R.id.calendar_list);
		
		CalendarAdapter adapter = new CalendarAdapter(getActivity());
		listView.setAdapter(adapter);
		
		return rootView;
	}
	
	private char getWeekDayChar(Calendar calendar) {
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
	
	private class CalendarAdapter extends ArrayAdapter<Time> {
				
		private final Context context;
		private final List<Time> times;
		
		public CalendarAdapter(Context context) {
			super(context, R.layout.calendar_row_layout);
			this.context = context;
			times = new ArrayList<Time>();
			
			Time current = Time.START;
			while (current.before(Time.END)) {
				times.add(current);
				current = current.timeAfter(30);
			}
			this.addAll(times);
		}
		
		int colorIndex = 0;
		private final Integer[] colors = {
			Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN,
			Color.CYAN, Color.MAGENTA
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
			
			WeekdayTime wt = new WeekdayTime(dayOfWeekChar, times.get(position));
			
			List<Meeting> meetings = profile.meetingsAt(wt);
			for (Meeting meeting : meetings) {
				Button button = new Button(context);
				button.setGravity(Gravity.TOP);
				if (!meetingNamePosition.containsKey(meeting) ||
						meetingNamePosition.get(meeting) == position) {
					button.setText(meeting.getSubject().getSubjectNumber() + meeting.getTypeString());
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
			}

		    return rowView;
		}
	}
}
