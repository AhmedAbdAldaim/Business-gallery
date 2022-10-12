package com.example.businesgalleryadmin.LocalDB;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalSession {

    private static final String PREF_NAME = "AcademicTraining";
    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String Role = "role";
    private static final String isSessionCreated = "isSessionCreated";

    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor editor;

    public LocalSession(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = mPreferences.edit();
    }

    public void createSession(String token,String Id,String name,String email,String password,String role) {
        editor.putBoolean(LocalSession.isSessionCreated, true);
        editor.putString(LocalSession.TOKEN,token);
        editor.putString(LocalSession.ID, Id);
        editor.putString(LocalSession.NAME,name);
        editor.putString(LocalSession.EMAIL,email);
        editor.putString(LocalSession.PASSWORD,password);
        editor.putString(LocalSession.Role,role);
        editor.apply();
        editor.commit();
    }


    public static Boolean getIsSessionCreated() {
        return mPreferences.getBoolean(LocalSession.isSessionCreated, false);
    }


    public static String getId() {
        return mPreferences.getString(ID,"");
    }
    public static String getName() {
        return mPreferences.getString(NAME,"");
    }
    public static String getEmail() {
        return mPreferences.getString(EMAIL,"");
    }
    public static String getPassword() {
        return mPreferences.getString(PASSWORD,"");
    }
    public static String getToken() {
        return mPreferences.getString(TOKEN,"");
    }
    public static String getRole() {
        return mPreferences.getString(Role,"");
    }

    public static void clearSession()
    {
            editor.clear();
            editor.apply();
            editor.commit();
    }
}
