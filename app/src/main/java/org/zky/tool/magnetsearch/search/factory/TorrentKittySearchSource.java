package org.zky.tool.magnetsearch.search.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.search.SearchEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.torrentkitty.tv/search/KIRD-061/1
 * Created by zhangkun on 2017/4/8.
 */

public class TorrentKittySearchSource implements SearchSource {
    private static final String url= UrlConstants.TORRENTKITTY_SEARCH_URL;

    private static final String name = "torrent kitty";

    private SearchEntityDao searchEntityDao;

    @Override
    public List<SearchEntity> parse(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementById("archiveResult");
        Elements tbody = table.getElementsByTag("tbody");
        Elements trs = tbody.get(0).getElementsByTag("tr");

        List<SearchEntity> list = new ArrayList<>();
        for (Element tr : trs) {
            Elements a = tr.getElementsByTag("a");
            if (!a.toString().equals("")) {
                String href = a.get(0).attr("href");
                String title = a.get(0).attr("title");
                String size = "--";
                Elements date = tr.getElementsByClass("date");
                String convertDate = date.text();

                SearchEntity searchEntity = new SearchEntity(href, title, size, convertDate);

                //TODO 这里把数据加入到了数据库 不应该这么做
                if (searchEntityDao == null) {
                    searchEntityDao = MagnetSearchApp.getInstanse().getDaoSession().getSearchEntityDao();
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
        return "/"+page;
    }

    @Override
    public String getKey(String key) {
        return key;
    }
}
