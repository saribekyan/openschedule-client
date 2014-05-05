package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

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
    public static List<String> getUserSubjectNumbers() {
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
    
    /**
     * Get the list containing the information about each subject
     * provided in subjectNumberList.
     * @param subjectNumberList The list of subject numbers that the user is interested in. If null, will return all classes.
     * @return the information about each subject in subjectNumberList,
     * or the empty list if error occurred during subject retrieval from the server.
     */
    public static List<Subject> getSubjects(List<String> subjectNumberList) {
    	List<Subject> result = new ArrayList<Subject>();
    	int skip = 0, bucket = 1000;
    	while (true) {
	        List<ParseObject> parseObjectList = null;
	        try {
	            // Retrieve subjects written in subjectNumberList.
	            ParseQuery<ParseObject> query = ParseQuery.getQuery("Subjects");
//                query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
//	            query.fromLocalDatastore();
	            if (subjectNumberList != null) {
	                query.whereContainedIn("number", subjectNumberList);
	                query.setMaxCacheAge(12*60*60*1000L);
	            } else {
	                query.whereGreaterThanOrEqualTo("classId", skip);
	                query.whereLessThan("classId", skip+bucket);
//	            	query.setSkip(skip);
	            	query.setLimit(bucket);
	            }
	            parseObjectList = query.find();
                ParseObject.pinAllInBackground(parseObjectList);
	        } catch (ParseException e) {
	            e.printStackTrace();
	            // Find failed, so we return empty list.
	            return result;
	        }
	        
	        for (ParseObject parseObject : parseObjectList) {
	            Subject subject = new Subject(parseObject.getString("number"),
	                                          parseObject.getString("name"),
	                                          parseObject.getString("description"));
	
	            for (MeetingType m : MeetingType.values()) {
	                JSONArray array;
	                if (m == MeetingType.LECTURE) {
	                    array = parseObject.getJSONArray("lectures");
	                } else if (m == MeetingType.RECITATION) {
	                    array = parseObject.getJSONArray("recitations");
	                } else {
	                    array = parseObject.getJSONArray("labs");
	                }
	                for (int j = 0; j < array.length(); j++) {
	                    try {
	                        String s = array.getString(j);
	                        // Replace many spaces with only one space.
	                        s = s.replaceAll("\\s+", " ");
	                        int x1 = s.indexOf(' ', 0);
	                        int x2 = s.lastIndexOf(' ');
	                        subject.addMeeting(m, s.substring(x2+1), s.substring(x1+1, x2));
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	                
	            }
	            result.add(subject);
	        }
	        Log.d("TAG", ""+skip+" "+parseObjectList.size());
//	        if (parseObjectList.size() < bucket) {
//	        	break;
//	        }
            skip += bucket;
            if (parseObjectList.size() < bucket) {
                break;
            }
    	}
    	
    	return result;
    }
    
//    private static String getSemester() {
//        Calendar calendar = new GregorianCalendar();
//        int month = calendar.get(GregorianCalendar.MONTH);
//        int year = calendar.get(GregorianCalendar.YEAR);
//        if (month < 5) {
//            return "sp"+year;
//        } else {
//            return "fa"+year;
//        }
//    }
}
