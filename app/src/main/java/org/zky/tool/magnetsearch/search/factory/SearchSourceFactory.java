package org.zky.tool.magnetsearch.search.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.utils.GetRes;

/**
 * 获取爬虫源的工厂类
 * Created by zhangkun on 2017/4/8.
 */

public class SearchSourceFactory {
    private static final String TAG = "SearchSourceFactory";

    /**
     * @param name the fully qualified name of the desired class.
     * @return instance
     */
    public static SearchSource getInstance(String name) {

        try {
            return (SearchSource) Class.forName(name).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new BtsoSearchSource();
        }

    }
}
