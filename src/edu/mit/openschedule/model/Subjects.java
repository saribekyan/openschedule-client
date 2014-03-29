package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

public class Subjects {
	private static final List<Subject> subjects = new ArrayList<Subject>();
	
	public static List<Subject> asList() {
		return subjects;
	}
	
	public static void addSubject(Subject subject) {
		subjects.add(subject);
	}
}
