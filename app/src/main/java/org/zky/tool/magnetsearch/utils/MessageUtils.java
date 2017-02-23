package org.zky.tool.magnetsearch.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.zky.tool.magnetsearch.R;

/**
 *
 * Created by zhangkun on 2017/2/23.
 */

public class MessageUtils {

    public static void snack(View view,@StringRes int message){
            Snackbar.make(view, GetRes.getString(message), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
    }

    public static void snack(View view, String message,String b, View.OnClickListener listener){
        Snackbar.make(view,message, BaseTransientBottomBar.LENGTH_LONG).setAction(b,listener).show();
    }
}
