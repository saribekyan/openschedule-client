package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	private final String number;
	private final String name;
	private final String description;
	
	private Meeting lecture = null;
	private List<Meeting> recitations = new ArrayList<Meeting>();
	private List<Meeting> labs = new ArrayList<Meeting>();
	
	public Subject(String number, String name, String description) {
		this.number = number;
		this.name = name;
		this.description = description;
	}
	
	public Subject addLecture(Meeting lecture) {
		if (this.lecture != null) {
			throw new IllegalStateException("lecture added twice");
		}
		if (lecture.meetingType != MeetingType.LECTURE) {
			throw new IllegalArgumentException("not a lecture");
		}
		this.lecture = lecture;
		return this;
	}
	
	public Subject addRecitation(Meeting recitation) {
		if (recitation.meetingType != MeetingType.RECITATION) {
			throw new IllegalArgumentException("not a recitation");
		}
		recitations.add(recitation);
		return this;
	}
	
	public Subject addLab(Meeting lab) {
		if (lab.meetingType != MeetingType.LAB) {
			throw new IllegalArgumentException("not a lab");
		}		
		labs.add(lab);
		return this;
	}
	
	public String getSubjectName() {
		return name;
	}
	
	public String getSubjectNumber() {
		return number;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Meeting getLecture() {
		return lecture;
	}
	
	public Meeting getRecitation(int i) {
		return recitations.get(i);
	}
	
	public Meeting getLab(int i) {
		return labs.get(i);
	}
	
	public enum MeetingType {
		LECTURE,
		RECITATION,
		LAB
	};
	
	public class Meeting {
		private MeetingType meetingType;
		private String location;
		private List<WeekdayTime> meetingWeekdayTimes;
		
		public Meeting(MeetingType meetingType, String location) {
			this.meetingType = meetingType;
			this.location = location;
			this.meetingWeekdayTimes = new ArrayList<WeekdayTime>();
		}
		
		public Meeting add(WeekdayTime weekdayTime) {
			meetingWeekdayTimes.add(weekdayTime);
			return this;
		}
		
		public boolean hasMeetingAt(WeekdayTime weekdayTime) {
			for (WeekdayTime wt : meetingWeekdayTimes) {
				if (wt.isOverlapping(weekdayTime)) {
					return true;
				}
			}
			return false;
		}
		
		public Subject getSubject() {
			return Subject.this;
		}
		
		@Override
		public String toString() {
			switch (meetingType) {
			case LECTURE:
				return number + " " + "Lec";
			case RECITATION:
				return number + " " + "Rec";
			case LAB:
				return number + " " + "Lab";
			default:
				throw new IllegalStateException("you've added an enum value");
			}
		}
	}
}
