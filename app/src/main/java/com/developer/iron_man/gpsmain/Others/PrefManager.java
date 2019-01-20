package com.developer.iron_man.gpsmain.Others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sagar on 21/6/17.
 */

@SuppressLint("CommitPrefEdits")

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Shared pref file name
    private static final String PREF_NAME = "GpsTracker";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setToken(String name) {
        editor.putString("token", name);
        editor.commit();
    }

    public String getToken() {
        return pref.getString("token", null);
    }

    public void setUser(String user)
    {
        editor.putString("user", user);
        editor.commit();
    }

    public String getUser(){
        return pref.getString("user",null);
    }

    public void setMarkers(String markers)
    {
        editor.putString("marker", markers);
        editor.commit();
    }

    public String getMarkers(){
        return pref.getString("marker",null);
    }

    public void setUsername(String username)
    {
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString("username",null);
    }

    public void setLocationF(String location)
    {
        editor.putString("location", location);
        editor.commit();
    }

    public String getLocationF(){
        return pref.getString("location",null);
    }

    public void setNotificationFlag(String notify)
    {
        editor.putString("notify", notify);
        editor.commit();
    }

    public String getNotificationFlag(){
        return pref.getString("notify",null);
    }

    public void setFragmentFlag(String frag)
    {
        editor.putString("frag", frag);
        editor.commit();
    }

    public String getFragmentFlag(){
        return pref.getString("frag",null);
    }

 public void setEmegencyContact(String flag)
    {
        editor.putString("emergencyContact", flag);
        editor.commit();
    }

    public String getEmegencyContact(){
        return pref.getString("emergencyContact",null);
    }

    public void setUserId(String id)
    {
        editor.putString("id", id);
        editor.commit();
    }

    public String getUserId(){
        return pref.getString("id",null);
    }

    public void setDriver(String driver)
    {
        editor.putString("driver", driver);
        editor.commit();
    }

    public String getDriver(){
        return pref.getString("driver",null);
    }

    public void setQRFlag(String qr)
    {
        editor.putString("qr", qr);
        editor.commit();
    }

    public String getQRFlag(){
        return pref.getString("qr",null);
    }



}
