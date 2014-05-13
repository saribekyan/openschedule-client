package edu.mit.openschedule.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Task implements Comparable<Task> {
	
	public static final String[] TASKS = {"Problem Set", "Project", "Paper", "Lab", "Hands on"};
	
	private static int taskIds = 0;
	private final int taskId;
	
	private String name;
	
	private Calendar personalDeadline;
	private Calendar classDeadline;
	private Double othersSpent;
	private Double userSpent;
	private String submitLocation;
	private String subjectNumber;
	
	public enum Status {
		UNFINISHED, FINISHED, SUBMITTED
	};
	
	private Status status;
	
	public Task(String subjectNumber, String taskName, Calendar classDeadline) {
	    this.subjectNumber = subjectNumber;
	    this.name = taskName;
		this.classDeadline = classDeadline;
		this.taskId = taskIds++;
		personalDeadline = Calendar.getInstance();
		personalDeadline.setTime(classDeadline.getTime());
		othersSpent = null;
		userSpent = null;
		submitLocation = "N/A";
		status = Status.UNFINISHED;
	}
	
	public Task setPersonalDeadline(Calendar personalDeadline) {
		this.personalDeadline = personalDeadline;
		return this;
	}
	
	public Task changeClassDeadline(Calendar cal) {
		this.classDeadline = cal;
		return this;
	}
	
	public Task setOthersSpent(Double othersSpent) {
		this.othersSpent = othersSpent;
		return this;
	}
	
	public Task setSubmitLocation(String submitLocation) {
		this.submitLocation = submitLocation;
		return this;
	}
	
	public Task finish(double spent) {
		if (status != Status.UNFINISHED) {
			throw new IllegalStateException("The status was"+status.toString()+" instead of UNFINISHED");
		}
		status = Status.FINISHED;
		userSpent = spent;
		return this;
	}
	
	public Task submit() {
		if (status != Status.FINISHED) {
			throw new IllegalStateException();
		}
		status = Status.SUBMITTED;
		return this;
	}
	
	public String getName() {
	    return name;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getPersonalDeadlineString() {
		if (status != Status.UNFINISHED) {
			return "Finished";
		}
		return new SimpleDateFormat("MM/dd@hh:mmaa", Locale.getDefault())
					.format(personalDeadline.getTime());
	}

	public Calendar getClassDeadline() {
	    return classDeadline;
	}
	
	public String getClassDeadlineString() {
		return new SimpleDateFormat("MM/dd@hh:mmaa", Locale.getDefault())
					.format(classDeadline.getTime());
	}

	public String getOthersSpentString() {
		if (othersSpent == null) {
			return "N/A";
		}
		return String.format(Locale.getDefault(), "%.1f hours", othersSpent);
	}
	
	public String getUserSpentString() {
		if (status == Status.UNFINISHED) {
			throw new IllegalStateException();
		}
		return String.format(Locale.getDefault(), "%.1f hours", userSpent);
	}
	
	public double getUsersSpent() {
	    if (status == Status.UNFINISHED) {
            throw new IllegalStateException();
        }
        return userSpent;
	}
	
	public String getSubmitLocation() {
		return submitLocation;
	}
	
	@Override
	public int compareTo(Task another) {
		if (personalDeadline.before(another.personalDeadline)) {
			return -1;
		}
		if (personalDeadline.after(another.personalDeadline)) {
			return 1;
		}
		return 0;
	}

	public int getId() {
		return taskId;
	}
	
	public String getSubjectNumber() {
        return subjectNumber;
    }
}
