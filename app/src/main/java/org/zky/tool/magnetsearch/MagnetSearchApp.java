package org.zky.tool.magnetsearch;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.zky.tool.magnetsearch.greendao.gen.DaoMaster;
import org.zky.tool.magnetsearch.greendao.gen.DaoSession;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.GreenDaoManager;
import org.zky.tool.magnetsearch.utils.MessageUtils;


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
        MessageUtils.init(this);
        GreenDaoManager.init(this);

    }

    public static MagnetSearchApp getInstance(){
        return instance;
    }

}
