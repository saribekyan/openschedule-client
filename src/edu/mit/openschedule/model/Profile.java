package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

import edu.mit.openschedule.model.Subject.Meeting;

public class Profile {
	private String name;
	private List<Subject> subjects;
	
	private List<Boolean> lectureFlag; // indicates whether the students goes to lecture. 
	private List<Integer> recitationNumber;
	private List<Integer> labNumber;
	
	public List<Subject.Meeting> meetingsAt(WeekdayTime wt) {
		List<Subject.Meeting> meetings = new ArrayList<Subject.Meeting>();
		for (int i = 0; i < subjects.size(); ++i) {
			Subject subject = subjects.get(i);
			if (lectureFlag.get(i)) {
				Meeting lecture = subject.getLecture();
				if (lecture != null) {
					
				}
			}
		}
		return meetings;
	}
}
