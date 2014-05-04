package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.mit.openschedule.model.Subjects.MeetingType;

public class ParseServer {

    /**
     * @return the list containing subject numbers that the student is taking.
     * For example: ["6.004", "6.005", "18.02", "21F.222"].
     * Returns null if some error happens.
     */
    public static List<String> getSubjectNumbers() {
        List<String> result = new ArrayList<String>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        try {
            ParseObject parseObject = query.getFirst();
            JSONArray array = (JSONArray)parseObject.getJSONArray("subjects");
            for (int i = 0; i < array.length(); i++) {
                try {
                    result.add(array.getString(i));
                } catch (JSONException e) { return null; }
            }
        } catch (ParseException e) {
            return null;
        }
        return result;
    }
    
    public static List<Subject> getSubjects(List<String> subjectNumberList) {
        List<Subject> result = new ArrayList<Subject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subjects");
        query.whereContainedIn("number", subjectNumberList);
        try {
            List<ParseObject> parseObjectList = query.find();
            for (ParseObject parseObject : parseObjectList) {
                Subject subject = new Subject(parseObject.getString("number"), parseObject.getString("name"), parseObject.getString("description"));
                JSONArray array = null;
                try {
                    array = parseObject.getJSONArray("lectures");
                    for (int j = 0; j < array.length(); j++) {
                        String s = array.getString(j);
                        s = s.replaceAll("\\s+", " ");
                        int x1 = s.indexOf(' ', 0);
                        int x2 = s.lastIndexOf(' ');
                        try {
                            subject.addMeeting(MeetingType.LECTURE, s.substring(x2+1), s.substring(x1+1, x2));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                } catch (JSONException e) { return null; }
                result.add(subject);
            }
        } catch (ParseException e) {
            return null;
        }
        return result;
    }
}
