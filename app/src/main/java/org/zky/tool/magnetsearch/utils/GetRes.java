package org.zky.tool.magnetsearch.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * get resources simply
 * you need add init in app
 * Created by zhan9 on 2016/12/15.
 */

public class GetRes {
    private static Resources sResources;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
        sResources = context.getApplicationContext().getResources();
    }

    @NonNull
    public static String getString(@StringRes int id) {
        return sResources.getString(id);
    }

    @NonNull
    public static String getString(@StringRes int id, Object... formatArgs) {
        return sResources.getString(id, formatArgs);
    }

    @Nullable
    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(@DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return sContext.getDrawable(id);
        return sResources.getDrawable(id);

    }

    @SuppressWarnings("deprecation")
    public static void setClipboard(String s){
        ClipboardManager manager = (ClipboardManager) sContext.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(s);
    }

    public static void inputMethodToggle(){
        InputMethodManager imm = (InputMethodManager) sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //隐藏键盘
    public static void hideSoftKeyboard(View context) {
        InputMethodManager inputMethodManager = (InputMethodManager)sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getWindowToken(), 0);
    }
}
