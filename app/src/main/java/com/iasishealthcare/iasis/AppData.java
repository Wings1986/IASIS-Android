package com.iasishealthcare.iasis;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by iGold on 22.02.15.
 */
public class AppData {
    public static final String PREFERENCES = "preferences";
    
    public static final String KEY_FAVORITE_LOCATION = "favoriteLocation";
    public static final String KEY_FAVORITE_HOSPITAL = "favoriteHospital";

    public Context context;

    private static AppData instance;

    public static String kServerURL = "http://directory.iasishealthcare.com/";

    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    
    public void init(Context context) {
        this.context = context;
    }

    public int getFavouriteHospital() {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (sp.contains(KEY_FAVORITE_HOSPITAL)) {
            return sp.getInt(KEY_FAVORITE_HOSPITAL, -1);
        }
        return -1;
    }
    public void setFavoriteHospital(int hospitalID) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(KEY_FAVORITE_HOSPITAL, hospitalID);
        edit.commit();
    }

    public String getFavouriteLocation() {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (sp.contains(KEY_FAVORITE_LOCATION)) {
            return sp.getString(KEY_FAVORITE_LOCATION, "");
        }
        return "";
    }
    public void setFavoriteLocation(String userID) {
    	SharedPreferences sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(KEY_FAVORITE_LOCATION, userID);
        edit.commit();
    }


    // json data
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
