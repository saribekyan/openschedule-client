package edu.mit.openschedule.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.parse.ParseUser;

import edu.mit.openschedule.CustomApplication;
import edu.mit.openschedule.model.Subjects.MeetingType;

/**
 * This class is responsible for storing to and loading from the locally stored user profile.
 */
public class LocalUserProfile {

    public static JSONObject localUserProfile = new JSONObject();

    /**
     * Loads the data from local disk to the UserProfile class.
     * Things like chosen recitations and laboratories are loaded.
     * <br><br>
     * UserProfile must be populated with the subjects that
     * the user is taking before calling this function. <br>
     * Also, the user must be logged in.
     * @param context Application context
     * @param username User's kerberos ID
     */
    public static void loadUserProfile() {
        try {
            String username = ParseUser.getCurrentUser().getUsername();
            FileInputStream in = CustomApplication.context.openFileInput("user_profile_"+username+".json");
            localUserProfile = new JSONObject(new Scanner(in).useDelimiter("\\A").next());
            JSONObject meetings_json;
            for (MeetingType m : MeetingType.values()) {
                if (m == MeetingType.LECTURE) {
                    meetings_json = localUserProfile.optJSONObject("Lectures");
                } else if (m == MeetingType.RECITATION) {
                    meetings_json = localUserProfile.optJSONObject("Recitations");
                } else {
                    meetings_json = localUserProfile.optJSONObject("Labs");
                }
                if (meetings_json == null) continue;
                for (@SuppressWarnings("rawtypes") Iterator it = meetings_json.keys(); it.hasNext(); ) {
                    String subjectNumber = (String)it.next();
                    int meetingIndex = meetings_json.getInt(subjectNumber);
                    UserProfile.getUserProfile().setMeeting(subjectNumber, m, meetingIndex);
                }
            }
            
        } catch (FileNotFoundException e) {
            Log.d("TAG", "User profile couldn't be found");
            localUserProfile = new JSONObject();
        } catch (JSONException e) {
            Log.e("TAG", "User profile couldn't be parsed", e);
            localUserProfile = new JSONObject();
        }
    }
    
    public static void setMeeting(String subjectNumber, MeetingType type, int meetingIndex) {
        try {
            JSONObject meetings_json;
            // Find out the key
            String key;
            if (type == MeetingType.LECTURE) {
                key = "Lectures";
            } else if (type == MeetingType.RECITATION) {
                key = "Recitations";
            } else {
                key = "Labs";
            }
            // Get the map for the key if it exists, otherwise create it
            if (localUserProfile.has(key)) {
                meetings_json = localUserProfile.getJSONObject(key);
            } else {
                meetings_json = new JSONObject();
                localUserProfile.put(key, meetings_json);
            }
            // Put new value in the map
            meetings_json.put(subjectNumber, meetingIndex);
            // Save the file to the local disk.
            String username = ParseUser.getCurrentUser().getUsername();
            FileOutputStream out = CustomApplication.context.openFileOutput("user_profile_"+username+".json", Context.MODE_PRIVATE);
            out.write(localUserProfile.toString().getBytes());
            out.close();
        } catch (JSONException e) {
            Log.e("TAG", "User profile couldn't be parsed", e);
        } catch (FileNotFoundException e) {
            Log.d("TAG", "User profile couldn't be found");
        } catch (IOException e) {
            Log.d("TAG", "User profile couldn't be written");
        }
    }
}
