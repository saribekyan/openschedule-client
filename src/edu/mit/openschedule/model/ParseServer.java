package edu.mit.openschedule.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.mit.openschedule.R;
import edu.mit.openschedule.model.Subjects.MeetingType;

public class ParseServer {

    private static List<Subject> subjects = null;
    
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
     * Loads whole subject list from the resources to the memory, in background
     * @param context Application context
     */
    public static void loadSubjectListInBackground(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadSubjectList(context);
            }
        }).start();
    }
    
    /**
     * Loads whole subject list from the resources to the memory, if it's not loaded already.
     * @param context Application context
     * @return The list of subjects in the semester
     */
    public static synchronized List<Subject> loadSubjectList(Context context) {
        if (subjects != null) {
            return subjects;
        }
        subjects = new ArrayList<Subject>();
        try {
            // Read the subjects listing from the resources file and parse it into JSONObject
            JSONObject subjects_json = new JSONObject(new Scanner(context.getResources().openRawResource(R.raw.subjects_sp2014)).useDelimiter("\\A").next());
            for (@SuppressWarnings("rawtypes") Iterator it = subjects_json.keys(); it.hasNext(); ) {
                JSONObject subject_json = subjects_json.getJSONObject((String)it.next());
                subjects.add(_getSubject(subject_json.getString("number"),
                                        subject_json.getString("name"),
                                        subject_json.getString("description"),
                                        subject_json.getJSONArray("lectures"),
                                        subject_json.getJSONArray("recitations"),
                                        subject_json.getJSONArray("labs")));
            }
        } catch (NotFoundException e) {
            Log.e("TAG", "Subject List couldn't be found", e);
        } catch (JSONException e) {
            Log.e("TAG", "Subject List couldn't be parsed", e);
        }
        subjects = Collections.unmodifiableList(subjects);
        return subjects;
    }
    
    /** Returns Subject class initialized with the given parameters. */
    private static Subject _getSubject(String number, String name, String description, JSONArray lectures, JSONArray recitations, JSONArray labs) {
        Subject subject = new Subject(number, name, description);
        for (MeetingType m : MeetingType.values()) {
            JSONArray array;
            if (m == MeetingType.LECTURE) {
                array = lectures;
            } else if (m == MeetingType.RECITATION) {
                array = recitations;
            } else {
                array = labs;
            }
            for (int j = 0; j < array.length(); j++) {
                try {
                    String s = array.getString(j);
                    // Replace many spaces with only one space.
                    s = s.replaceAll("\\s+", " ");
                    int x1 = s.indexOf(' ', 0);
                    int x2 = s.lastIndexOf(' ');
                    subject.addMeeting(m, s.substring(x2 + 1),
                            s.substring(x1 + 1, x2));
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }

        }
        return subject;
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
