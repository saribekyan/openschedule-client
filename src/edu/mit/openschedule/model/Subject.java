package edu.mit.openschedule.model;

import java.sql.Time;
import java.util.List;

public class Subject {
	private String subjectNumber;
	private String subjectName;
	private String subjectDescription;
	
	private Meeting lecture;
	private List<Meeting> recitations;
	private List<Meeting> labs;
	
	public Meeting getLecture() {
		return lecture;
	}
	
	public Meeting getRecitation(int i) {
		return recitations.get(i);
	}
	
	public Meeting getLab(int i) {
		return labs.get(i);
	}
	
	private enum MeetingType {
		LECTURE,
		RECITATION,
		LAB
	};
	
	public class Meeting {
		private MeetingType meetingType;
		private String location;
		private List<WeekdayTime> meetingWeekdayTimes;
		
		public boolean hasMeetingAt(WeekdayTime wt) {
			return true;
		}
	}
}
