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
		Subjects.addSubject(this);
	}
	
	public Subject setRating(float rating) {
		this.rating = rating;
		return this;
	}
	
	public Subject addLecture(Meeting lecture) {
		if (lecture.meetingType != MeetingType.LECTURE) {
			throw new IllegalArgumentException("not a lecture");
		}
		meetings.get(MeetingType.LECTURE).add(lecture);
		return this;
	}
	
	public Subject addRecitation(Meeting recitation) {
		if (recitation.meetingType != MeetingType.RECITATION) {
			throw new IllegalArgumentException("not a recitation");
		}
		meetings.get(MeetingType.RECITATION).add(recitation);
		return this;
	}
	
	public Subject addLab(Meeting lab) {
		if (lab.meetingType != MeetingType.LAB) {
			throw new IllegalArgumentException("not a lab");
		}		
		meetings.get(MeetingType.LAB).add(lab);
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
	
	public Meeting getLecture(int i) {
		return meetings.get(MeetingType.LECTURE).get(i);
	}
	
	public Meeting getRecitation(int i) {
		return meetings.get(MeetingType.RECITATION).get(i);
	}
	
	public Meeting getLab(int i) {
		return meetings.get(MeetingType.LAB).get(i);
	}
	
	public class Meeting {
		
		private MeetingType meetingType;
		private String location;
		private List<WeekdayTime> meetingWeekdayTimes;
		private String mitFormatWeekdayTime;
		
		public Meeting(MeetingType meetingType, String location, String mitFormatWeekdayTime) {
			this.meetingType = meetingType;
			this.location = location;
			this.meetingWeekdayTimes = new ArrayList<WeekdayTime>();
			this.mitFormatWeekdayTime = mitFormatWeekdayTime;
			
			String days = "MTWRF";
			int i = 0;
			while (days.indexOf(mitFormatWeekdayTime.charAt(i)) != -1)
				++i;
			int k = mitFormatWeekdayTime.indexOf('-');
			for (int j = 0; j < i; ++j) {
				if (k != -1)
					this.add(new WeekdayTime(mitFormatWeekdayTime.charAt(j), mitFormatWeekdayTime.substring(i, k), 
							mitFormatWeekdayTime.substring(k+1)));
				else
					this.add(new WeekdayTime(mitFormatWeekdayTime.charAt(j), mitFormatWeekdayTime.substring(i)));
			}
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
