package com.needle.soniaditi380.books;

/**
 * Created by Aditya on 26-Jan-18.
 */
import android.content.Context;

import android.content.SharedPreferences;

import android.net.Uri;
import android.preference.PreferenceManager;

import java.net.URI;
import java.net.URL;


/**

 * Created by DELL STORE on 5/17/2017.

 */

//Shared Preferences allow you to save and retrieve data in the form of key,value pair.

//Use Shared Preference , When user logged into your application store login status into

// sharedPreference and clear sharedPreference when user click on logged Out.

//Check every time when user enter into application

// if user status from shared Preference is true then no need to login otherwise move to login logic.



public class SaveSharedPrefrence {

    static final String PREF_USER_NAME= "username";
    static final String PREF_AUTH_CODE= "code";
    static final String PREF_USER_ID= "userid";
    static final String PREF_USER_PHOTO="photo";
    static final String PREF_NAME="name";
    static final String REFRESH_TOKEN= "refresh_token";
    static final String ACCESS_TOKEN ="access_token";



    static SharedPreferences getSharedPreferences(Context ctx) {

        // getDefltSharedPreferences method ,Gets a SharedPreferences instance that points to the default file

        // that is used by the preference framework in the given context.

        return PreferenceManager.getDefaultSharedPreferences(ctx);

    }



// In Signin activity if user login successful then set UserName using setUserName() function.

    public static void setUserName(Context ctx, String userName)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_USER_NAME, userName);

        editor.commit();

    }
    // In Signin activity if user login successful then set access token

    public static void setAccessToken(Context ctx, String accesstok)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(ACCESS_TOKEN, accesstok);

        editor.commit();

    }

    // In Signin activity if user login successful then set UserName using setUserName() function.

    public static void setName(Context ctx, String Name)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_NAME, Name);

        editor.commit();

    }

    // In Signin activity if user login successful then set token

    public static void setAuthCode(Context ctx, String auth_code)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_AUTH_CODE, auth_code);

        editor.commit();

    }





    // In Signin activity if user login successful then set refreshtoken

    public static void setRefreshToken(Context ctx, String token)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(REFRESH_TOKEN, token);

        editor.commit();

    }


// In Signin activity if user login successful then set UserId

    public static void setUserId(Context ctx, String userId)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_USER_ID, userId);

        editor.commit();

    }

    // In Signin activity if user login successful then set photourl

    public static void setPhoto(Context ctx,Uri photo)

    {

        //You can save something in the sharedpreferences by using SharedPreferences.Editor class.

        // You will call the edit method of SharedPreference instance and will receive it in an editor object.

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString(PREF_USER_PHOTO, photo.toString());

        editor.commit();

    }

    //get the current user name

    public static String getUserName(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially username key has value of length zero

        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");

    }


    //get the current name

    public static String getName(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially username key has value of length zero

        return getSharedPreferences(ctx).getString(PREF_NAME, "");

    }

    //get the current token

    public static String getAuthCode(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially username key has value of length zero

        return getSharedPreferences(ctx).getString(PREF_AUTH_CODE, "");

    }

    //get the current user id

    public static String getUserId(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially username key has value of length zero

        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");

    }


    //get the current user name

    public static String getphoto(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially username key has value of length zero

        return getSharedPreferences(ctx).getString(PREF_USER_PHOTO, "");

    }


    //get the current user refersh token

    public static String getRefreshToken(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially refresh token key has value of length zero

        return getSharedPreferences(ctx).getString(REFRESH_TOKEN, "");

    }


    //get the current user access token

    public static String getAccessToken(Context ctx)

    {

        //In order to use shared preferences, you have to call a method getSharedPreferences()

        // that returns a SharedPreference instance pointing to the file

        // that contains the values of preferences.



        //initially access token key has value of length zero

        return getSharedPreferences(ctx).getString(ACCESS_TOKEN, "");

    }

    //clear all stored data if user log out

    public static void clear(Context ctx)

    {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.clear(); //clear all stored data

        editor.commit();

    }



}