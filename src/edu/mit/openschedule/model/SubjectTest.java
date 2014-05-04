package edu.mit.openschedule.model;

import android.test.ActivityInstrumentationTestCase2;
import edu.mit.openschedule.model.Subjects.MeetingType;
import edu.mit.openschedule.ui.AddSubjectActivity;

public class SubjectTest extends ActivityInstrumentationTestCase2<AddSubjectActivity> {

	public SubjectTest() {
		super(AddSubjectActivity.class);
	}

	public void testAfternoonOneHourClass() {
		Subject s = new Subject("6.001", "algo", "algo is cool");
		s.addMeeting(MeetingType.LECTURE, "1-123", "MWF1");
		assertEquals("1-123", s.getMeeting(MeetingType.LECTURE, 0).getLocationString());
		assertEquals('M', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).weekday);
		assertEquals('W', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).weekday);
		assertEquals('F', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).weekday);
		
		assertEquals("01:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).startTime.toString());
		assertEquals("01:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).startTime.toString());
		assertEquals("01:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).startTime.toString());
		
		assertEquals("02:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).endTime.toString());
		assertEquals("02:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).endTime.toString());
		assertEquals("02:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).endTime.toString());
	}
	
	public void testMorningOneHourClass() {
		Subject s = new Subject("6.002", "algo", "algo is cool");
		s.addMeeting(MeetingType.LECTURE, "1-123", "MWF10:30");
		assertEquals("1-123", s.getMeeting(MeetingType.LECTURE, 0).getLocationString());
		assertEquals('M', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).weekday);
		assertEquals('W', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).weekday);
		assertEquals('F', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).weekday);
		
		assertEquals("10:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).startTime.toString());
		assertEquals("10:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).startTime.toString());
		assertEquals("10:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).startTime.toString());
		
		assertEquals("11:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).endTime.toString());
		assertEquals("11:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).endTime.toString());
		assertEquals("11:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(2).endTime.toString());
	}
	
	public void testNoonOneHourClass() {
		Subject s = new Subject("6.003", "algo", "algo is cool");
		s.addMeeting(MeetingType.LECTURE, "1-123", "TR11:30");
		assertEquals("1-123", s.getMeeting(MeetingType.LECTURE, 0).getLocationString());
		assertEquals('T', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).weekday);
		assertEquals('R', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).weekday);
		
		assertEquals("11:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).startTime.toString());
		assertEquals("11:30am", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).startTime.toString());
		
		assertEquals("12:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).endTime.toString());
		assertEquals("12:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).endTime.toString());
	}
	
	public void testAfternoonLongClass() {
		Subject s = new Subject("6.003", "algo", "algo is cool");
		s.addMeeting(MeetingType.LECTURE, "1-123", "TR1-2:30");
		assertEquals("1-123", s.getMeeting(MeetingType.LECTURE, 0).getLocationString());
		assertEquals('T', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).weekday);
		assertEquals('R', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).weekday);
		
		assertEquals("01:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).startTime.toString());
		assertEquals("01:00pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).startTime.toString());
		
		assertEquals("02:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).endTime.toString());
		assertEquals("02:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).endTime.toString());
	}
	
	public void testEveningLongClass() {
		Subject s = new Subject("6.003", "algo", "algo is cool");
		s.addMeeting(MeetingType.LECTURE, "1-123", "TR EVE (5:30-8:30 PM)");
		assertEquals("1-123", s.getMeeting(MeetingType.LECTURE, 0).getLocationString());
		assertEquals('T', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).weekday);
		assertEquals('R', s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).weekday);
		
		assertEquals("05:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).startTime.toString());
		assertEquals("05:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).startTime.toString());
		
		assertEquals("08:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(0).endTime.toString());
		assertEquals("08:30pm", s.getMeeting(MeetingType.LECTURE, 0).getWeekdayTimes().get(1).endTime.toString());
	}
}
