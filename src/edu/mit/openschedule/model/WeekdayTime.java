package edu.mit.openschedule.model;


public class WeekdayTime {
	
	public final char weekday; // MTWRF
	public final Time startTime;
	public final Time endTime;
	
	public WeekdayTime(char weekday, String startTimeString, String endTimeString) {
		this.weekday = weekday;
		this.startTime = new Time(startTimeString);
		this.endTime = new Time(endTimeString);
	}
	
	public WeekdayTime(char weekday, Time startTime) {
		this.weekday = weekday;
		this.startTime = startTime;
		this.endTime = startTime.timeAfter(60);
	}
	
	public WeekdayTime(char weekday, String startTimeString) {
		this(weekday, new Time(startTimeString));
	}
	
	public boolean isOverlapping(WeekdayTime other) {
		return this.weekday == other.weekday &&
				(!this.startTime.after(other.startTime) && this.endTime.after(other.startTime) || 
				 !other.startTime.after(this.startTime) && other.endTime.after(this.startTime));
	}
}
