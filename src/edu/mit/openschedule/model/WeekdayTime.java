package edu.mit.openschedule.model;

import java.sql.Time;

public class WeekdayTime {
	public final char weekday; // MTWRF
	public final Time startTime;
	public final Time endTime;
	
	public WeekdayTime(char weekday, String startTimeString, String endTimeString) {
		this.weekday = weekday;
		startTimeString = startTimeString.replace('.', ':') + ":00";
		startTimeString = startTimeString.replace('.', ':') + ":00";
		this.startTime = Time.valueOf(startTimeString);
		this.endTime = Time.valueOf(startTimeString);
	}
	
	public boolean isOverlap(WeekdayTime other) {
		return false;
	}
}
