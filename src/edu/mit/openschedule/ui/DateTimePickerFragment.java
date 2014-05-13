package edu.mit.openschedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import edu.mit.openschedule.R;

public class DateTimePickerFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_date_time_picker, container,
				false);
		
		
		getActivity().setResult(-1);
		final DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
		
		final TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
		timePicker.setCurrentHour(23);
		timePicker.setCurrentMinute(59);
		
		final Button selectButton = (Button) rootView.findViewById(R.id.date_time_activity_select_button);
		selectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.putExtra("year", datePicker.getYear());
				intent.putExtra("month", datePicker.getMonth());
				intent.putExtra("day", datePicker.getDayOfMonth());
				intent.putExtra("hour", timePicker.getCurrentHour());
				intent.putExtra("minute", timePicker.getCurrentMinute());
				intent.putExtra("task_id", getActivity().getIntent().getIntExtra("task_id", 0));
				
				getActivity().setResult(0, intent);
				getActivity().finish();
			}
		});
		return rootView;
	}
}
