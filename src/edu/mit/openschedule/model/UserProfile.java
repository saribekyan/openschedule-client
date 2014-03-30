package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Subjects.MeetingType;
import edu.mit.openschedule.model.Task.Status;

public class UserProfile {
	private static UserProfile singletonProfile = null;
	
	private String name;
	private List<Subject> subjects;
	
	private Map< MeetingType, List<Integer> > meetingNumber =
			new HashMap<MeetingType, List<Integer> >();
	
	private List<Task> tasks =
			new ArrayList<Task>();
	
	// Singleton
	private UserProfile() {
		meetingNumber.put(MeetingType.LECTURE, new ArrayList<Integer>());
		meetingNumber.put(MeetingType.RECITATION, new ArrayList<Integer>());
		meetingNumber.put(MeetingType.LAB, new ArrayList<Integer>());
		
		name = "Tsotne Tabidze";
		subjects = new ArrayList<Subject>();
		Subject s1 = new Subject("6.046", "Algo", "Also is cool");
		Meeting l1 = s1.new Meeting(MeetingType.LECTURE, "26-100", "TR9.30-11")
						.add(new WeekdayTime('T', "9:30", "11:00"))
						.add(new WeekdayTime('R', "9:30", "11"));
		Meeting r1 = s1.new Meeting(MeetingType.RECITATION, "36-156", "F3")
						.add(new WeekdayTime('F', "3", "4"));
		s1.addLecture(l1)
			.addRecitation(r1);
		Subjects.addSubject(s1);
		
		
		subjects.add(s1);
		meetingNumber.get(MeetingType.LECTURE).add(0);
		meetingNumber.get(MeetingType.RECITATION).add(0);
		meetingNumber.get(MeetingType.LAB).add(null);
		
//		Subject s4 = new Subject("6.046", "Algo", "Also is cool");
//		Meeting l4 = s4.new Meeting(MeetingType.LECTURE, "26-100")
//						.add(new WeekdayTime('T', "9:30", "11:00"))
//						.add(new WeekdayTime('R', "9:30", "11"));
//		Meeting r4 = s4.new Meeting(MeetingType.RECITATION, "36-156")
//						.add(new WeekdayTime('F', "3", "4"));
//		s4.addLecture(l4)
//			.addRecitation(r4);
//		
//		lectureFlag.add(true);
//		subjects.add(s4);
//		recitationNumber.add(0);
//		labNumber.add(null);

		Subject s2 = new Subject("6.036", "ML", "ML is cool");
		Meeting l2 = s2.new Meeting(MeetingType.LECTURE, "10-250", "TR10.30-12")
						.add(new WeekdayTime('T', "10:30", "12:00"))
						.add(new WeekdayTime('R', "10:30", "12:00"));
		Meeting r2 = s2.new Meeting(MeetingType.RECITATION, "36-156", "F3")
						.add(new WeekdayTime('F', "3"));
		Meeting lab2 = s2.new Meeting(MeetingType.LAB, "32-082", "F5")
						.add(new WeekdayTime('F', "5"));
		s2.addLecture(l2)
			.addRecitation(r2)
			.addLab(lab2);
		Subjects.addSubject(s2);
		
		subjects.add(s2);
		meetingNumber.get(MeetingType.LECTURE).add(0);
		meetingNumber.get(MeetingType.RECITATION).add(0);
		meetingNumber.get(MeetingType.LAB).add(0);
		
//		Subject s3 = new Subject("6.036", "ML", "ML is cool");
//		Meeting l3 = s3.new Meeting(MeetingType.LECTURE, "10-250")
//						.add(new WeekdayTime('T', "10:30", "12:00"))
//						.add(new WeekdayTime('R', "10:30", "12:00"));
//		Meeting r3 = s3.new Meeting(MeetingType.RECITATION, "36-156")
//						.add(new WeekdayTime('F', "3"));
//		s3.addLecture(l3)
//			.addRecitation(r3);
//		
//		lectureFlag.add(true);
//		subjects.add(s3);
//		recitationNumber.add(0);
//		labNumber.add(null);
		
		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online"));
		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online").finish(10, 30));
		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online").finish(10, 30).submit());
	}
	
	public static UserProfile getUserProfile() {
		if (singletonProfile == null) {
			singletonProfile = new UserProfile();
		}
		return singletonProfile;
	}
	
	public List<Meeting> meetingsAt(WeekdayTime weekdayTime) {
		List<Meeting> meetings = new ArrayList<Meeting>();
		for (MeetingType type : MeetingType.values()) {
			for (int i = 0; i < subjects.size(); ++i) {
				Subject subject = subjects.get(i);
				
				if (meetingNumber.get(type).get(i) != null) {
					Meeting lecture = subject.getLecture(meetingNumber.get(type).get(i));
					assert lecture != null;
					if (lecture.hasMeetingAt(weekdayTime)) {
						meetings.add(lecture);
					}
				}
			}
		}
		return meetings;
	}
	
	public List<Task> getNotSubmittedTasksSorted() {
		List<Task> resultList = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.getStatus() != Status.SUBMITTED) {
				resultList.add(task);
			}
		}
		Collections.sort(resultList);
		return resultList;
	}
	
	public List<Task> getSubmittedTasksSorted() {
		List<Task> resultList = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.getStatus() == Status.SUBMITTED) {
				resultList.add(task);
			}
		}
		Collections.sort(resultList);
		return resultList;
	}
	
	public List<Subject> getSubjectList() {
		return subjects;
	}

	public Integer getMeetingIdFor(Subject subject, MeetingType type) {
		for (int i = 0; i < subjects.size(); ++i) {
			if (subjects.get(i).getId() == subject.getId()) {
				return meetingNumber.get(type).get(i);
			}
		}
		throw new IllegalArgumentException("no such class is taken");
	}

	public boolean isTaking(Subject subject) {
		for (Subject mySubject : subjects) {
			if (mySubject.getId() == subject.getId()) {
				return true;
			}
		}
		return false;
	}
}
