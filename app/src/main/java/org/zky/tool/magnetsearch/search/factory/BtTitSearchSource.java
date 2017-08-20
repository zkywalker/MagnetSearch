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
 * http://www.bttit.com/torrent/hello_3.html
 * TODO 分页结构有问题，所以这个网站不能搜 _index 的东西。。。
 * html结构照抄的btso
 * Created by zhangkun on 2017/4/8.
 */

public class BtTitSearchSource implements SearchSource {

    private static final String url = UrlConstants.BTSO_SEARCH_URL;

    private static final String name = "bttit";

    private SearchEntityDao searchEntityDao;

    @Override
    public List<SearchEntity> parse(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByClass("data-list").get(0);
        Element btsowlist = element.getElementsByClass("btsowlist").get(0);
        Elements rows = btsowlist.getElementsByClass("row");
        List<SearchEntity> list = new ArrayList<>();
        for (Element row : rows) {
            Elements a = row.getElementsByTag("a");
            if (!a.toString().equals("")) {
                String href = a.attr("href");
                String title = a.attr("title");
                Elements divSize = row.getElementsByClass("size");
                String size = divSize.get(0).text();
                Elements divDate = row.getElementsByClass("date");
                String convertDate = divDate.get(0).text();

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
        return "_"+page+".html";
    }
}
