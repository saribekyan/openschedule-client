package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.test.InstrumentationTestCase;
import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Subjects.MeetingType;

public class ParseServerTest extends InstrumentationTestCase {

    public void test_getSubjects() {
        List<Subject> all_subjects = ParseServer.loadSubjectList();
        List<Subject> subjects = new ArrayList<Subject>();
        for (Subject subject : all_subjects) {
            if (subject.getNumber().equals("6.046")) {
                subjects.add(subject);
            }
        }
        assertEquals(1, subjects.size());
        Subject subject = subjects.get(0);
        assertEquals("6.046 Design and Analysis of Algorithms", subject.getFullName().toString());
        List<String> meetings = new ArrayList<String>();
        for (MeetingType mt : MeetingType.values()) {
            for (Meeting m : subject.getAllMeetings(mt)) {
                meetings.add(m.getTypeString() + " " + 
                             m.getTimeString() + " " + 
                             m.getLocationString());
            }
        }
        List<String> expectedMeetings = new ArrayList<String>(Arrays.asList("Lecture TR9.30-11 34-101",
                                                                            "Recitation F10 36-112",
                                                                            "Recitation F11 36-112",
                                                                            "Recitation F12 36-112",
                                                                            "Recitation F1 36-112",
                                                                            "Recitation F2 36-112",
                                                                            "Recitation F3 36-112",
                                                                            "Recitation F12 36-144"));
        assertEquals(expectedMeetings.size(), meetings.size());
        for (int i = 0; i < meetings.size(); i++) {
            assertEquals(expectedMeetings.get(i), meetings.get(i));
        }
    }
}
