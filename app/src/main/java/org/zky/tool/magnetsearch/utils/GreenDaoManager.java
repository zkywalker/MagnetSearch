package org.zky.tool.magnetsearch.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.zky.tool.magnetsearch.greendao.gen.DaoMaster;
import org.zky.tool.magnetsearch.greendao.gen.DaoSession;

public class GreenDaoManager {

    private static GreenDaoManager sInstance;

    private DaoMaster.DevOpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;


    private GreenDaoManager(Context context){
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(context, "history-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static void init(Context context){
        if (sInstance == null) {
            sInstance = new GreenDaoManager(context);
        }
    }

    public static GreenDaoManager getInstance() {
        if (sInstance == null) {
            throw new IllegalArgumentException("plz init GreenDaoManager.");
        }
        return sInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
