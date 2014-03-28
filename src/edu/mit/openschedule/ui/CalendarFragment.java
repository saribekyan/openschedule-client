package edu.mit.openschedule.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.mit.openschedule.R;

public class CalendarFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		
		TextView date = (TextView) rootView.findViewById(R.id.calendar_date);
		Date today = Calendar.getInstance().getTime();
		date.setText(SimpleDateFormat.getDateInstance().format(today));
		
		final ListView listView = (ListView) rootView.findViewById(R.id.calendar_list);
		final String[] times = {"8am", "9am"};		
		CalendarAdapter adapter = new CalendarAdapter(
				getActivity(), R.layout.calendar_row_layout, times);
		listView.setAdapter(adapter);
		
		return rootView;
	}
	
	private class CalendarAdapter extends ArrayAdapter<String> {

		private final String[] times;
		private final Context context;
		
		public CalendarAdapter(Context context, int resource, String[] times) {
			super(context, R.layout.calendar_row_layout, times);
			this.context = context;
			this.times = times;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			RelativeLayout rowView = (RelativeLayout)
					inflater.inflate(R.layout.calendar_row_layout, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.text_calendar_time);
		    textView.setText(times[position]);
		    
		    Button button = new Button(context);
		    button.setText("hello");
		    
		    RelativeLayout.LayoutParams params =
		    		new RelativeLayout.LayoutParams(
		    				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    params.addRule(RelativeLayout.RIGHT_OF, textView.getId());
		    rowView.addView(button, params);
		    
		    return rowView;
		}
	}
}
