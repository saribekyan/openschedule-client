package edu.mit.openschedule.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Task implements Comparable<Task> {
	
	private final int id;
	private static int taskIds = 0;
	
	private String name;
	
	private Calendar personalDeadline;
	private Calendar classDeadline;
	private Double othersSpent;
	private Double userSpent;
	private String submitLocation;
	
	public enum Status {
		UNFINISHED, FINISHED, SUBMITTED
	};
	
	private Status status;
	
	public Task(String name,
			Calendar classDeadline) {
		this.name = name;
		this.classDeadline = classDeadline;
		
		personalDeadline = Calendar.getInstance();
		personalDeadline.setTime(classDeadline.getTime());
		othersSpent = null;
		userSpent = null;
		submitLocation = "N/A";
		status = Status.UNFINISHED;
		this.id = taskIds++;
	}
	
	public int getId() {
		return id;
	}
	
	public Task setPersonalDeadline(Calendar personalDeadline) {
		this.personalDeadline = personalDeadline;
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
	
	public Task finish(int hoursSpent, int minutesSpent) {
		if (status != Status.UNFINISHED) {
			throw new IllegalStateException();
		}
		status = Status.FINISHED;
		userSpent = hoursSpent + minutesSpent / 60.0;
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
		return new SimpleDateFormat("MM/dd hh:mm", Locale.getDefault())
					.format(personalDeadline.getTime());
	}

	public String getClassDeadlineString() {
		return new SimpleDateFormat("MM/dd hh:mm", Locale.getDefault())
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
}
