package org.zky.tool.magnetsearch.search;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.search.factory.SearchSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 解析 数据源 的转换器
 * Created by zhangkun on 2017/2/21.
 */

public class SearchResponseBodyConverter implements Converter<ResponseBody, List<SearchEntity>> {
    private static final String TAG = "SearchResponseBodyConverter";

    private SearchSource mSearchSource;

    public SearchResponseBodyConverter(SearchSource searchSource){
        mSearchSource =searchSource;
    }

    @Override
    public List<SearchEntity> convert(ResponseBody value) throws IOException {
        try {
            return mSearchSource.parse(new String(value.bytes()));
        } finally {
            value.close();
        }
    }


}