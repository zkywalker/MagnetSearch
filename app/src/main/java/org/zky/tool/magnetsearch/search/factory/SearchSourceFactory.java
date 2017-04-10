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

    public static SearchSource getInstance(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String source = preferences.getString(GetRes.getString(R.string.key_search_source), BtsoSearchSource.class.getName());
        try {
            return (SearchSource) Class.forName(source).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO 否则返回一个默认的搜索源？
        return null;
    }
}
