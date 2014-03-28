package edu.mit.openschedule.model;

import java.util.Locale;

import android.net.ParseException;

public class Time {

	public static final Time START = new Time("9:00");
	public static final Time END = new Time("18:00");
	
	private int hour, minute;

	public Time(String timeString) {
		String[] split = timeString.replace('.', ':').split(":");
		hour = Integer.parseInt(split[0]);
		if (hour < 9) {
			hour += 12;
		}
		if (split.length > 1) {
			minute = Integer.valueOf(split[1]);
		} else {
			minute = 0;
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
		String res = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
		if (hour < 12) {
			res += "am";
		} else {
			res += "pm";
		}
		return res;
	}
}
