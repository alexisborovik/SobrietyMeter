package com.alexis.borovik.sobrietymeter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexis on 01.11.2016.
 */

public class Preferences {
    private static final String APP_REFERENCES = "MySetting";
    public static final String APP_REFERENCES_Effect_OF_ALCOHOL = "EffectOfAlcohol";
    public static final String APP_REFERENCES_WEIGHT = "Weight";
    private static final String APP_HAS_VISITED = "visited";
    private static final String APP_REFERENCES_ALCOHOL_IN_BODY = "alcoholInBody";
    private static final String LAST_CHANGED = "lastChanged";

    Context context;
    private SharedPreferences preferences;

    Preferences(Context context) {
        this.context = context;
    }

    public void setPreferences(int weightPref, int effectOfAlcPref) {
        this.preferences = context.
                getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt(APP_REFERENCES_WEIGHT, weightPref);
        editor.putInt(APP_REFERENCES_Effect_OF_ALCOHOL, effectOfAlcPref);
        editor.apply();
    }

    void setWeightOfAlcoholInBody(float wight){
        this.preferences = context.
                getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putFloat(Preferences.APP_REFERENCES_ALCOHOL_IN_BODY, wight);
        editor.putString(Preferences.LAST_CHANGED,Tools.getTimeAsString());
        editor.apply();
    }

    void setLastChangedTime(){
        this.preferences = context.
                getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(Preferences.LAST_CHANGED,Tools.getTimeAsString());
        editor.apply();
    }

    long getDiffInTimeWithLastModified()
    {
        SharedPreferences preferences =
                context.getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        String lastm = preferences.getString(Preferences.LAST_CHANGED,Tools.getTimeAsString());
        String now = Tools.getTimeAsString();
        return Tools.getDateDiff(Tools.convertStringToDate(lastm),Tools.convertStringToDate(now));
    }

    float getWeightOfAlcoholInBody(){
        SharedPreferences preferences =
                context.getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        return preferences.getFloat
                (Preferences.APP_REFERENCES_ALCOHOL_IN_BODY, Context.MODE_PRIVATE);
    }

    public int getPreferences(String appReferenceFlag) {
        SharedPreferences preferences =
                context.getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        return  preferences.getInt(appReferenceFlag, -1);
    }

    public boolean hasChanged(){
        SharedPreferences sp =
                context.getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(APP_HAS_VISITED, false);
    }

    public void setChanged(boolean visited){
        SharedPreferences sp =
                context.getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(APP_HAS_VISITED, visited);
        editor.apply();
    }
}
