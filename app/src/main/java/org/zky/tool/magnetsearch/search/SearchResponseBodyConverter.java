package org.zky.tool.magnetsearch.search;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 *
 * Created by zhangkun on 2017/2/21.
 */

public class SearchResponseBodyConverter implements Converter<ResponseBody, List<SearchEntity>> {
    private static final String TAG = "StringResponseBodyConve";

    private SearchEntityDao searchEntityDao;

    @Override
    public List<SearchEntity> convert(ResponseBody value) throws IOException {
        try {
            return parse(new String(value.bytes()));
        } finally {
            value.close();
        }
    }

    private List<SearchEntity> parse(String html){
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByClass("data-list").get(0);
        Elements rows = element.getElementsByClass("row");
        List<SearchEntity> list = new ArrayList<>();
        for (Element row : rows) {
            Elements a = row.getElementsByTag("a");
            if (!a.toString().equals("")){
                String href = a.attr("href");
                String title = a.attr("title");
                Elements divSize = row.getElementsByClass("size");
                String size = divSize.get(0).text();
                Elements divDate = row.getElementsByClass("date");
                String convertDate = divDate.get(0).text();

                SearchEntity searchEntity = new SearchEntity(href, title, size, convertDate);

                list.add(searchEntity);

                if (searchEntityDao==null){
                    searchEntityDao = MagnetSearchApp.getInstanse().getDaoSession().getSearchEntityDao();
                }

                searchEntityDao.insertOrReplace(searchEntity);
            }

        }
        return list;
    }
}