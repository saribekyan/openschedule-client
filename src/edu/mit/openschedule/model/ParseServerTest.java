package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import android.util.Log;
import edu.mit.openschedule.model.Subject.Meeting;
import edu.mit.openschedule.model.Subjects.MeetingType;

public class ParseServerTest extends TestCase {

    public void test_getSubjects() {
        List<Subject> subjects = ParseServer.getSubjects(new ArrayList<String>(Arrays.asList("6.006", "6.046")));
        for (Subject s : subjects) {
            Log.d("TAG", s.getFullName().toString());
            for (MeetingType mt : MeetingType.values()) {
                for (Meeting m : s.getAllMeetings(mt)) {
                    Log.d("TAG",  m.getTypeString()
                          + " " + m.getTimeString()
                          + " " + m.getLocationString());
                    
                }
            }
        }
    }
}
