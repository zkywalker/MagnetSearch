package org.zky.tool.magnetsearch.utils.http;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zky.tool.magnetsearch.search.SearchEntity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Created by zhangkun on 2017/2/20.
 */

public class DownloadBtsoThread extends Thread {
    private static final String TAG = "DownloadBtsoThread";

    private static final String BTSO_SEARCH_URL = "https://btso.pw/search/";

    private static final int ERROR_CODE_SEARCH_KEY = 0;
    private static final int ERROR_CODE_TIMEOUT = 1;

    private String mSearchKey;

    private Connection mConnection;

    private Callback mCallback;

    public DownloadBtsoThread(String searchKey,Callback callback){
        mSearchKey = searchKey;
        mCallback = callback;

        initConnection(mSearchKey);
    }

    public void initConnection(String searchKey) {
        mConnection = Jsoup.connect(BTSO_SEARCH_URL + searchKey);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        map.put("host","btso.pw");
        mConnection.headers(map).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                "AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
        mConnection.timeout(1000);

    }

    @Override
    public void run() {
        if (checkSearchKey(mSearchKey)) {
            try {
                mCallback.onStart();
                Connection.Response response = mConnection.execute();

                if (response.statusCode()==200){
                    mCallback.onParse(response.body());
                    Document document = Jsoup.parse(response.body());
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

                            list.add(new SearchEntity(href, title, size, convertDate));
                        }

                    }
                    mCallback.onSuccess(list);

                }
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException){
                    mCallback.onFail(ERROR_CODE_TIMEOUT);
                }
                Log.e(TAG, "run: ",e );

            }
        }else {
            mCallback.onFail(ERROR_CODE_SEARCH_KEY);
        }
    }

    public boolean checkSearchKey(String mSearchKey) {

        return true;
    }

    public void setCallback(Callback callback){
        mCallback =callback;
    }

    public interface Callback{
        public void onStart();
        public void onParse(String html);
        public void onSuccess(List<SearchEntity> entity);
        public void onFail(int code);
    }
}
