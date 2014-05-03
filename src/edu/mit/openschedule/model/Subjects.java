package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subjects {
	public enum MeetingType {
		LECTURE,
		RECITATION,
		LAB
	}
	
	@SuppressWarnings("serial")
	public static final Map<MeetingType, String> MEETING_NAME =
			Collections.unmodifiableMap(
					new HashMap<MeetingType, String>() { {
							put(MeetingType.LECTURE, "Lecture");
							put(MeetingType.RECITATION, "Recitation");
							put(MeetingType.LAB, "Lab");
						}
					});

	private static final List<Subject> subjects = new ArrayList<Subject>();
	
	public static List<Subject> asList() {
		return subjects;
	}
	
	public static void addSubject(Subject subject) {
		subjects.add(subject);
	}

	public static Subject getSubject(Integer id) {
		return subjects.get(id);
	}
	
	public static int size() {
	    return subjects.size();
	}
}
