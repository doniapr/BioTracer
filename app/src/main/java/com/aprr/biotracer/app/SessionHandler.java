package com.aprr.biotracer.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.aprr.biotracer.konstanta.ProfilHandler;

import java.util.ArrayList;
import java.util.Date;

public class SessionHandler {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "nama";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private ArrayList<ProfilHandler> profilArrayList;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param email
     * @param fullName
     */
    public void loginUser(String email, String fullName, int role, int user_id) {
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_FULL_NAME, fullName);
        mEditor.putInt(KEY_ROLE, role);
        mEditor.putInt(KEY_USER_ID, user_id);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setRole(mPreferences.getInt(KEY_ROLE, 0));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setUser_id(mPreferences.getInt(KEY_USER_ID, 0));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }


}
