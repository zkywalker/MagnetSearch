package org.zky.tool.magnetsearch.search.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.utils.GreenDaoManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * http://www.btwhat.net/search/batman/1-3.html
 * Created by zhangkun on 2017/4/8.
 */

public class BtWhatSearchSource implements SearchSource {

    private static final String url = UrlConstants.BTWHAT_SEARCH_URL;

    private static final String name = "btwhat";

    private SearchEntityDao searchEntityDao;

    @Override
    public List<SearchEntity> parse(String html) {


        Document document = Jsoup.parse(html);
        Element element = document.getElementById("wall");
        Elements rows = element.getElementsByClass("search-item");
        List<SearchEntity> list = new ArrayList<>();
        for (Element row : rows) {
            Elements a = row.getElementsByClass("item-title").get(0).getElementsByTag("h3").get(0).getElementsByTag("a");
            Element script = a.get(0).getElementsByTag("script").get(0);
            String title = script.data().substring(34, script.data().length() - 3);
            title = title.replace("\"","");
            title = title.replace("+","");
//            title = title.replace("%3cb%3e","<b>");
//            title = title.replace("%3c%2fb%3e","</b>");
            try {
                title = URLDecoder.decode(title,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//            String title = a.get(0).text();

            String href = a.attr("href");
            href = href.split("/")[2];
            href = href.replace(".html","");

            Elements item = row.getElementsByClass("item-bar");
            Elements spans = item.get(0).getElementsByTag("span");

            Element bdate = spans.get(1).getElementsByTag("b").get(0);
            String convertDate = bdate.text();

            Element bsize = spans.get(2).getElementsByTag("b").get(0);
            String size = bsize.text();

            SearchEntity searchEntity = new SearchEntity(href, title, size, convertDate);

            //TODO 这里把数据加入到了数据库 不应该这么做
            if (searchEntityDao == null) {
                searchEntityDao = GreenDaoManager.getInstance().getDaoSession().getSearchEntityDao();
            }
            List<SearchEntity> searchEntities = searchEntityDao.loadAll();
            if (searchEntities.size() != 0) {
                list.add(searchEntity);
                boolean contains = false;
                for (SearchEntity s : searchEntities) {
                    if (s.equals(searchEntity)) {
                        list.remove(searchEntity);
                        list.add(s);
                        contains = true;
                        break;
                    }
                }
                if (!contains)
                    searchEntityDao.insert(searchEntity);
            } else
                searchEntityDao.insert(searchEntity);

        }
        return list;
    }

    @Override
    public String getBaseUrl() {
        return url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPage(int page) {
        return "/" + page + "-3.html";
    }

    @Override
    public String getKey(String key) {
        return key;
    }
}
