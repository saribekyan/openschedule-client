package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Subjects.MeetingType;
import edu.mit.openschedule.model.Task.Status;

public class UserProfile {
	private static UserProfile singletonProfile = null;
	
	private List<Subject> subjects = new ArrayList<Subject>();
	
	private Map< MeetingType, List<Integer> > meetingNumber =
			new HashMap<MeetingType, List<Integer> >();
	
	private List<Task> tasks = new ArrayList<Task>();
	
	private List<String> subjectNumbers;
	
	// Singleton
	private UserProfile() {
		meetingNumber.put(MeetingType.LECTURE, new ArrayList<Integer>());
		meetingNumber.put(MeetingType.RECITATION, new ArrayList<Integer>());
		meetingNumber.put(MeetingType.LAB, new ArrayList<Integer>());

//		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online"));
//		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online").finish(10, 30));
//		tasks.add(new Task("18.06 pset 4", Calendar.getInstance()).setOthersSpent(5.0).setSubmitLocation("online").finish(10, 30).submit());
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
	
	public void setSubjects(List<String> subjectNumbers) {
	    this.subjects = new ArrayList<Subject>();
	    this.subjectNumbers = new ArrayList<String>(subjectNumbers);
	    for(int i = 0; i < subjectNumbers.size(); ++i) {
	    	subjects.add(Subjects.findByNumber(subjectNumbers.get(i)));
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

	public Integer getMeetingIdFor(Subject subject, MeetingType type) {
		for (int i = 0; i < subjects.size(); ++i) {
			if (subjects.get(i) == subject) { // no need for .equals, because for each class there should be only one instance of Subject
				return meetingNumber.get(type).get(i);
			}
		}
		throw new IllegalArgumentException("no such class is taken");
	}

	public boolean isTaking(Subject subject) {
		for (Subject mySubject : subjects) {
			if (mySubject == subject) {
				return true;
			}
		}
		return false;
	}
	
	public void setMeeting(String subjectNumber, MeetingType type, int meetingIndex) {
		for (int i = 0; i < subjects.size(); ++i) {
			if (subjects.get(i).getNumber().equals(subjectNumber)) {
				meetingNumber.get(type).set(i, meetingIndex);
				// Cache this meeting locally, so that the next time it can be retrieved.
				LocalUserProfile.setMeeting(subjectNumber, type, meetingIndex);
			}
		}
	}

	public Task getTask(int taskId) {
		for (Task task : tasks) {
			if (task.getId() == taskId) {
				return task;
			}
		}
		throw new RuntimeException("no such id");
	}

	public List<String> getSubjectsString() {
		List<String> subjectsString = new ArrayList<String>();
		for (Subject subject : subjects) {
			subjectsString.add(subject.getNumber() + " " + subject.getName());
		}
		return subjectsString;
	}

	public boolean addTask(Task newTask, boolean addOnServer) {
		for (int i = 0; i < tasks.size(); i++) {
		    if (tasks.get(i).getName().equals(newTask.getName())) {
		        tasks.remove(i);
		    }
		}
		tasks.add(newTask);
		// Add your task in the global database
		if (addOnServer) {
	        ParseObject newTaskObject = new ParseObject("Tasks");
	        newTaskObject.put("SubjectNumber", newTask.getSubjectNumber());
	        newTaskObject.put("Deadline", newTask.getClassDeadline().getTime());
	        newTaskObject.put("TaskName", newTask.getName());
            newTaskObject.put("Location", newTask.getSubmitLocation());
            newTaskObject.put("Username", ParseUser.getCurrentUser().getUsername());
	        newTaskObject.setACL(new ParseACL());
	        newTaskObject.saveInBackground();
		}
		
		return true;
	}
	
	public void setTasks(List<Task> tasks, boolean addOnServer) {
	    for (Task task : tasks) {
	        addTask(task, addOnServer);
	    }
	}
	
	public List<String> getSubjectNumbers() {
        return new ArrayList<String>(subjectNumbers);
    }
}
