package edu.mit.openschedule.ui;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of TimePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);

	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		int taskId = getArguments().getInt("task_id");
		if (taskId < 0) { // new task
			AddTaskFragment.year = year;
			AddTaskFragment.month = month;
			AddTaskFragment.day = day;
			AddTaskFragment.selectDateButton.setText(month + "/" + day);
		} else {
//			UserProfile.getUserProfile().getTask(taskId).set?
		}
	}
}