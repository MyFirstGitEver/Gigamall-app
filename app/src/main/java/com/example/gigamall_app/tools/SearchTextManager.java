package com.example.gigamall_app.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.gigamall_app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchTextManager {
    public static synchronized void insertNewHint(Context context, String hint){
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        int pointer = preferences.getInt("pointer", -1);

        SharedPreferences.Editor editor = preferences.edit();

        pointer = (pointer + 1) % 4;
        editor.putString("hint " + pointer, hint);
        editor.putInt("pointer", pointer);

        editor.apply();

        int i = 3;
    }

    public static synchronized void storeLastTerm(Context context, String term){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        preferences.edit().putString("term", term).apply();
    }

    public static List<String> getHints(Context context){
        List<String> hints = new ArrayList<>();

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        for(int i=0;i<4;i++){
            String hint = preferences.getString("hint " + i, null);

            if(hint != null){
                hints.add(hint);
            }
        }

        return hints;
    }

    public static String getLastTerm(Context context){
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString("term", "");
    }
}
