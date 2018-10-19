package org.zky.tool.magnetsearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import org.zky.tool.magnetsearch.base.BaseThemeActivity;
import org.zky.tool.magnetsearch.R;


/**
 * Created by zkywalker on 2017/1/1.
 * package:org.zky.zky.utils
 */

public class PreferenceUtils {

    public static String  getValue(Context context, @StringRes int str){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(GetRes.getString(str),"");
    }

    public static String getTheme(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(GetRes.getString(R.string.key_theme), BaseThemeActivity.THEME_DEFAULT);
    }

    public static void setTheme(Context context, String theme){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(GetRes.getString(R.string.key_theme),theme).apply();
    }
}
