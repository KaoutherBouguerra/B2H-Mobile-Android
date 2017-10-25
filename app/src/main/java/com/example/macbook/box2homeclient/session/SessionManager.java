package com.example.macbook.box2homeclient.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.macbook.box2homeclient.LoginActivity;
import com.example.macbook.box2homeclient.MainActivity;
import com.example.macbook.box2homeclient.models.User;

import java.util.HashMap;

/**
 * Created by macbook on 17/10/2017.
 */

public class SessionManager {

    private static final String PREF_NAME = "namePref";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    public static final String KEY_Acesstoken = "access_token";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_ADRESSE = "address";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERNAME_CANO = "username_canonical";
    public static final String KEY_EMAIL_CANO = "email_canonical";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_SOCIETE = "nom_societe";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CREATION_DATE = "date_creation";
    public static final String KEY_STATUS = "statut";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final  String Key_UserPWD="PASSWORD";
    public static final  String Key_UserID="ID";
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String name, String email, String accessId) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // storing access token in pref
        editor.putString(KEY_Acesstoken, accessId);
        Log.v("accessId", ""+accessId);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        Log.v("key email", email);

        // commit changes
        editor.commit();
    }
    public void setUserDetails(String id, String adresse, String username,
                               String username_canonical, String email,
                               String email_canonical, String first_name, String last_name ,
                               String nom_societe,String phone,String photo,
                               String state,String country,String zip_code,
                               String type,String statut,String date_creation
                               ) {

        // storing user id in pref
        editor.putString(Key_UserID, id);
        // Storing name in pref
        editor.putString(KEY_ADRESSE, adresse);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USERNAME_CANO, username_canonical);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_EMAIL_CANO, email_canonical);
        editor.putString(KEY_FIRSTNAME, first_name);
        editor.putString(KEY_LASTNAME, last_name);
        editor.putString(KEY_SOCIETE, nom_societe);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PHOTO, photo);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_COUNTRY, country);
        editor.putString(KEY_ZIP_CODE, zip_code);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_STATUS, statut);
        editor.putString(KEY_CREATION_DATE, date_creation);
        Log.v("key email", email);

        // commit changes
        editor.commit();
    }
    // to get User id
    public void saveUserId(int userId){
        editor.putInt(Key_UserID,userId);
        editor.commit();


    }
    public HashMap<String, String > getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        //access token
        user.put(KEY_Acesstoken, pref.getString(KEY_Acesstoken, null));
        user.put(Key_UserID, pref.getString(Key_UserID, null));

        //  Log.v("Key_UserID", user.put(Key_UserID,pref.getString(Key_UserID,null)));
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ADRESSE, pref.getString(KEY_ADRESSE, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USERNAME_CANO, pref.getString(KEY_USERNAME_CANO, null));
        user.put(KEY_EMAIL_CANO, pref.getString(KEY_EMAIL_CANO, null));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, null));
        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, null));
        user.put(KEY_SOCIETE, pref.getString(KEY_SOCIETE, null));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null));
        user.put(KEY_STATE, pref.getString(KEY_STATE, null));
        user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));
        user.put(KEY_ZIP_CODE, pref.getString(KEY_ZIP_CODE, null));
        user.put(KEY_TYPE, pref.getString(KEY_TYPE, null));
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        user.put(KEY_CREATION_DATE, pref.getString(KEY_CREATION_DATE, null));




        // return user
        return user;
    }
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        // After logout redirect User to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String ,String> getUserPassword(){
        HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or User id
        usrId.put(Key_UserPWD, pref.getString(Key_UserPWD,null));
        return usrId;
    }

    public void saveUserPwd(String userPwd){
        editor.putString(Key_UserPWD,userPwd);
        editor.commit();
    }
    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    private User loggedUser;

    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            // user is  logged in redirect him to main Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            //  System.exit(0);
        }

    }


    public HashMap<String ,Integer> getUserId(){
        HashMap<String, Integer> usrId = new HashMap<String, Integer>();
        // customer or user id
        usrId.put(Key_UserID, pref.getInt(Key_UserID,0));
        return usrId;
    }
}
