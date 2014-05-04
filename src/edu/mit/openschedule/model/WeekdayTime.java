package edu.mit.openschedule.model;


public class WeekdayTime {
	
	public final char weekday; // MTWRF
	public final Time startTime;
	public final Time endTime;
	
	public WeekdayTime(char weekday, Time startTime) {
		this.weekday = weekday;
		this.startTime = startTime;
		this.endTime = startTime.timeAfter(30);
	}
	
	public WeekdayTime(char weekday, String startTimeString, String endTimeString, boolean eve) {
		this.weekday = weekday;
		this.startTime = new Time(startTimeString, eve);
		this.endTime = new Time(endTimeString, eve);
	}

	public WeekdayTime(char weekday, String startTimeString, boolean eve) {
		this.weekday = weekday;
		this.startTime = new Time(startTimeString, eve);
		this.endTime = startTime.timeAfter(60);
	}

	public boolean isOverlapping(WeekdayTime other) {
		return this.weekday == other.weekday &&
				(!this.startTime.after(other.startTime) && this.endTime.after(other.startTime) || 
				 !other.startTime.after(this.startTime) && other.endTime.after(this.startTime));
	}
}
