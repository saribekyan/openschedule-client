package edu.mit.openschedule.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.mit.openschedule.CustomApplication;
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
    public static void loadSubjectListInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadSubjectList();
            }
        }).start();
    }
    
    /**
     * Loads whole subject list from the resources to the memory, if it's not loaded already.
     * @param context Application context
     * @return The list of subjects in the semester
     */
    public static synchronized List<Subject> loadSubjectList() {
        if (subjects != null) {
            return subjects;
        }
        subjects = new ArrayList<Subject>();
        try {
            // Read the subjects listing from the resources file and parse it into JSONObject
            InputStream in = CustomApplication.context.getResources().openRawResource(R.raw.subjects_sp2014);
            JSONObject subjects_json = new JSONObject(new Scanner(in).useDelimiter("\\A").next());
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
    
    @SuppressWarnings("deprecation")
    public static List<Task> loadTaskList(List<String> classes) {
        List<Task> result = new ArrayList<Task>();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("subjectNumbers", classes);
            params.put("username", ParseUser.getCurrentUser().getUsername());
            HashMap<String, Object> tasks = ParseCloud.callFunction("getTaskList", params);
            for (String task_key : tasks.keySet()) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> task_map = (HashMap<String, Object>)tasks.get(task_key);
                Calendar deadline = new GregorianCalendar();
                deadline.setTime(new Date((String)task_map.get("Deadline")));
                String subjectNumber = (String)task_map.get("SubjectNumber");
                String taskName = (String)task_map.get("TaskName");
                String location = (String)task_map.get("Location");
                String submitted = (String)task_map.get("Submitted");
                double hoursSpent = Double.parseDouble((String)task_map.get("HoursSpent"));
                double othersSpent = Double.parseDouble((String)task_map.get("OthersSpent"));
                Task newTask = new Task(subjectNumber, taskName, deadline).setSubmitLocation(location);
                if (othersSpent > 0) {
                    newTask.setOthersSpent(othersSpent);
                }
                if (hoursSpent > 0) {
                    newTask.finish(hoursSpent);
                } else if (submitted.equals("true")) {
                    newTask.finish(hoursSpent);
                    newTask.submit();
                }
                result.add(newTask);
            }
        } catch (ParseException e) { }
        catch (RuntimeException e) { } // It's thrown when the thread is interrupted
        return result;
    }

}
