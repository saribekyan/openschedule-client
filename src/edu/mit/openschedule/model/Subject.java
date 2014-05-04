package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.openschedule.model.Subjects.MeetingType;

public class Subject {
	
	private Integer id = null;
	
	private final String number;
	private final String name;
	private final String description;
	
	private float rating;
	
	private Map<MeetingType, List<Meeting> > meetings = new HashMap<MeetingType, List<Meeting>>();
	
	public Subject(String number, String name, String description) {
		this.number = number;
		this.name = name;
		this.description = description;
		
		meetings.put(MeetingType.LECTURE, new ArrayList<Meeting>());
		meetings.put(MeetingType.RECITATION, new ArrayList<Meeting>());
		meetings.put(MeetingType.LAB, new ArrayList<Meeting>());
		
		this.id = Subjects.asList().size();
	}
	
	public Subject setRating(float rating) {
		this.rating = rating;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Subject addMeeting(MeetingType meetingType, String location, String mitFormatWeekdayTime) {
		meetings.get(meetingType).add(new Meeting(meetingType, location, mitFormatWeekdayTime));
		return this;
	}
	
	public class Meeting {
		
		private MeetingType meetingType;
		private String location;
		private List<WeekdayTime> meetingWeekdayTimes;
		private String mitFormatWeekdayTime;
		
		private Meeting(MeetingType meetingType, String location, String mitFormatWeekdayTime) {
			this.meetingType = meetingType;
			this.location = location;
			this.meetingWeekdayTimes = new ArrayList<WeekdayTime>();
			this.mitFormatWeekdayTime = mitFormatWeekdayTime;

			String meetTime;
			String days;
			boolean eve;
			
			if (mitFormatWeekdayTime.contains("EVE")) {
				int leftParen = mitFormatWeekdayTime.indexOf('(');
				int rightParen = mitFormatWeekdayTime.indexOf(')');
				meetTime = mitFormatWeekdayTime.substring(leftParen + 1, rightParen);
				meetTime = meetTime.substring(0, meetTime.indexOf(' '));
				days = mitFormatWeekdayTime.substring(0, mitFormatWeekdayTime.indexOf(' '));
				eve = true;
			} else {
				int i = 0;
				while (!Character.isDigit(mitFormatWeekdayTime.charAt(i))) {
					++i;
				}
				days = mitFormatWeekdayTime.substring(0, i);
				meetTime = mitFormatWeekdayTime.substring(i);
				eve = false;
			}
			String[] times = meetTime.split("-");
			for (int i = 0; i < days.length(); ++i) {
				if (times.length > 1) {
					meetingWeekdayTimes.add(new WeekdayTime(
							days.charAt(i),
							times[0],
							times[1],
							eve));
				} else {
					meetingWeekdayTimes.add(new WeekdayTime(
							days.charAt(i),
							times[0],
							eve));					
				}
			}
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
		
		public String getTypeString() {
			switch (meetingType) {
			case LECTURE:
				return "Lecture";
			case RECITATION:
				return "Recitation";
			case LAB:
				return "Lab";
			default:
				throw new IllegalStateException("you've added an enum value");
			}
		}

		public String getTimeString() {
			return mitFormatWeekdayTime;
		}

		public String getLocationString() {
			return location;
		}
		
		public List<WeekdayTime> getWeekdayTimes() {
			return meetingWeekdayTimes;
		}
	}

	public Integer getId() {
		return id;
	}

	public Meeting getMeeting(MeetingType type, Integer meetingId) {
		return meetings.get(type).get(meetingId);
	}

	public int getMeetingCount(MeetingType meetingType) {
		return meetings.get(meetingType).size();
	}
	
	/**
	 * @param type Type of the meetings to return
	 * @return All the meetings of the given type
	 */
	public List<Meeting> getAllMeetings(MeetingType type) {
	    return new ArrayList<Meeting>(meetings.get(type));
	}

	public float getRating() {
		return rating;
	}

	public CharSequence getFullName() {
		return number + " " + name;
	}
}
