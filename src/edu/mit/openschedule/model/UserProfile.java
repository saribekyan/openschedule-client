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
	
	public List<Subject> getSubjects() {
	    return new ArrayList<Subject>(subjects);
	}
	
	public void setSubjects(List<Subject> subjects) {
	    this.subjects = new ArrayList<Subject>(subjects);
	    for(int i=0;i<subjects.size(); i++) {
	        for (MeetingType m : MeetingType.values()) {
	            int count = subjects.get(i).getMeetingCount(m);
	            Integer value = null;
	            if (count == 1) {
	                value = 0;
	            } else if (count > 1) {
	                value = -1;
	            }
                meetingNumber.get(m).add(value);
	        }
	    }
	}
	
	public List<Meeting> meetingsAt(WeekdayTime weekdayTime) {
		List<Meeting> meetings = new ArrayList<Meeting>();
		for (MeetingType type : MeetingType.values()) {
			for (int i = 0; i < subjects.size(); ++i) {
				Subject subject = subjects.get(i);
				
				Integer id = meetingNumber.get(type).get(i);
				if (id != null && id != -1) {
					Meeting meeting = subject.getMeeting(type, id);
					if (meeting.hasMeetingAt(weekdayTime)) {
						meetings.add(meeting);
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

	public Task getTask(int taskId) {
		for (Task task : tasks) {
			if (task.getId() == taskId) {
				return task;
			}
		}
		throw new RuntimeException("no such id");
	}
}
