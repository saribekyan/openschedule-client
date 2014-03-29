package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Subject.MeetingType;
import edu.mit.openschedule.model.Task.Status;

public class UserProfile {
	private static UserProfile singletonProfile = null;
	
	private String name;
	private List<Subject> subjects;
	
	private List<Boolean> lectureFlag =
			new ArrayList<Boolean>(); // indicates whether the students goes to lecture. 
	private List<Integer> recitationNumber =
			new ArrayList<Integer>();
	private List<Integer> labNumber =
			new ArrayList<Integer>();
	
	private List<Task> tasks =
			new ArrayList<Task>();
	
	// Singleton
	private UserProfile() {
		name = "Tsotne Tabidze";
		subjects = new ArrayList<Subject>();
		Subject s1 = new Subject("6.046", "Algo", "Also is cool");
		Meeting l1 = s1.new Meeting(MeetingType.LECTURE, "26-100")
						.add(new WeekdayTime('T', "9:30", "11:00"))
						.add(new WeekdayTime('R', "9:30", "11"));
		Meeting r1 = s1.new Meeting(MeetingType.RECITATION, "36-156")
						.add(new WeekdayTime('F', "3", "4"));
		s1.addLecture(l1)
			.addRecitation(r1);
		
		lectureFlag.add(true);
		subjects.add(s1);
		recitationNumber.add(0);
		labNumber.add(null);
		
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
		Meeting l2 = s2.new Meeting(MeetingType.LECTURE, "10-250")
						.add(new WeekdayTime('T', "10:30", "12:00"))
						.add(new WeekdayTime('R', "10:30", "12:00"));
		Meeting r2 = s2.new Meeting(MeetingType.RECITATION, "36-156")
						.add(new WeekdayTime('F', "3"));
		Meeting lab2 = s2.new Meeting(MeetingType.LAB, "32-082")
						.add(new WeekdayTime('F', "5"));
		s2.addLecture(l2)
			.addRecitation(r2)
			.addLab(lab2);
		
		lectureFlag.add(true);
		subjects.add(s2);
		recitationNumber.add(0);
		labNumber.add(0);
		
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
		for (int i = 0; i < subjects.size(); ++i) {
			Subject subject = subjects.get(i);
			
			if (lectureFlag.get(i)) {
				Meeting lecture = subject.getLecture();
				assert lecture != null;
				if (lecture.hasMeetingAt(weekdayTime)) {
					meetings.add(lecture);
				}
			}
			
			if (recitationNumber.get(i) != null) {
				Meeting recitation = subject.getRecitation(recitationNumber.get(i));
				assert recitation != null;
				if (recitation.hasMeetingAt(weekdayTime)) {
					meetings.add(recitation);
				}
			}
			
			if (labNumber.get(i) != null) {
				Meeting lab = subject.getLab(labNumber.get(i));
				assert lab != null;
				if (lab.hasMeetingAt(weekdayTime)) {
					meetings.add(lab);
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
}
