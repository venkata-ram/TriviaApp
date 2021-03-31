package com.venkataram.mytriviaapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String TRIVIA_ID = "trivia_prefs";
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        this.sharedPreferences = activity.getSharedPreferences(TRIVIA_ID, Context.MODE_PRIVATE);
    }

    public void saveMaxScore(int score){
        int prev_score = sharedPreferences.getInt("high_score",0);

        if(score > prev_score) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high_score", score);
            editor.apply();
        }

    }

    public int getMaxScore(){
        int maxScore = sharedPreferences.getInt("high_score",0);
        return maxScore;
    }

    public void saveState(int index){
        sharedPreferences.edit().putInt("state_index",index).apply();
    }

    public int getStateIndex(){
        return sharedPreferences.getInt("state_index",0);
    }



}
