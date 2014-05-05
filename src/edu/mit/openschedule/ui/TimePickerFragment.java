package edu.mit.openschedule.ui;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import edu.mit.openschedule.model.UserProfile;

public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

	}
	
	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		int taskId = getArguments().getInt("task_id");
		if (taskId == -1) { // new task
			AddTaskFragment.hour = hour;
			AddTaskFragment.minute = minute;
			AddTaskFragment.selectTimeButton.setText(hour + ":" + minute);
		} else {
			UserProfile.getUserProfile().getTask(taskId).finish(hour, minute);
		}
	}
}
