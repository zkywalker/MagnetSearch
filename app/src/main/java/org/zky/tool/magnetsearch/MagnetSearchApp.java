package org.zky.tool.magnetsearch;

import android.app.Application;

import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.GreenDaoManager;
import org.zky.tool.magnetsearch.utils.ToastUtils;


/**
 * 包:org.zky.zky
 * Created by zhangkun on 2016/12/13.
 */

public class MagnetSearchApp extends Application {

    private static MagnetSearchApp instance;

    @Override
    public void onCreate() {

        super.onCreate();
        instance =this;

        GetRes.init(this);
        ToastUtils.init(this);
        GreenDaoManager.init(this);

    }

    public static MagnetSearchApp getInstance(){
        return instance;
    }

}
