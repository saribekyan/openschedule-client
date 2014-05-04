package edu.mit.openschedule.model;

import java.util.Locale;

public class Time {

	public static final Time START = new Time("8:00", false);
	public static final Time END = new Time("23:00", false);
	
	private int hour, minute;

	public Time(String timeString, boolean eve) {
		String[] split = timeString.replace('.', ':').split(":");
		hour = Integer.parseInt(split[0]);
		if (split.length > 1) {
			minute = Integer.valueOf(split[1]);
		} else {
			minute = 0;
		}
		
		if (eve || hour < 8) {
			hour += 12;
		}
	}
	
	public Time(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public boolean before(Time other) {
		return hour < other.hour || hour == other.hour && minute < other.minute;
	}

	boolean after(Time other) {
		return hour > other.hour || hour == other.hour && minute > other.minute;
	}

	public Time timeAfter(int minutes) {
		int m = minute + minutes;
		int h = hour + m / 60;
		m %= 60;
		return new Time(h, m);
	}

	@Override
	public String toString() {
		int hr = hour;
		if (hour > 12) {
			hr -= 12;
		}
		String res = String.format(Locale.getDefault(), "%02d:%02d", hr, minute);
		if (hour >= 12) {
			res += "pm";
		} else {
			res += "am";
		}
		return res;
	}
}
