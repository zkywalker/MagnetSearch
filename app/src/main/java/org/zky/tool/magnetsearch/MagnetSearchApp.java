package org.zky.tool.magnetsearch;

import android.app.Application;

import org.zky.tool.magnetsearch.utils.GetRes;


/**
 * 包:org.zky.zky
 * Created by zhangkun on 2016/12/13.
 */

public class MagnetSearchApp extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        GetRes.init(this);

    }
}
