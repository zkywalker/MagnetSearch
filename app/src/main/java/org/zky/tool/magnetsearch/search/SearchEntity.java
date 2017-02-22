package org.zky.tool.magnetsearch.search;

/**
 *
 * Created by zhangkun on 2017/2/20.
 */

public class SearchEntity {


    public SearchEntity(String href, String title, String size, String date) {
        this.href = href;
        this.title = title;
        this.size = size;
        this.date = date;
    }

    /**
     * href : eee
     * title : Brett
     * size : 1g
     * date : 2017-1-1
     */

    private String href;
    private String title;
    private String size;
    private String date;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "SearchEntity{" +
                "href='" + href + '\'' +
                ", title='" + title + '\'' +
                ", size='" + size + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
