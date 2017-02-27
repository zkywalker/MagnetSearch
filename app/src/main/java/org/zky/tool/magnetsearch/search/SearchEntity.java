package org.zky.tool.magnetsearch.search;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 *
 * Created by zhangkun on 2017/2/20.
 */

@Entity
public class SearchEntity {

    @Id
    private Long id;

    private boolean opened;

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public SearchEntity(String href, String title, String size, String date) {
        this.href = href;
        this.title = title;
        this.size = size;
        this.date = date;
    }

    @Generated(hash = 1882901940)
    public SearchEntity(Long id, boolean opened, String href, String title, String size,
            String date) {
        this.id = id;
        this.opened = opened;
        this.href = href;
        this.title = title;
        this.size = size;
        this.date = date;
    }

    @Generated(hash = 1021466028)
    public SearchEntity() {
    }

    /**
     * href : 磁力链地址
     * title : 标题
     * size : 1g
     * date : 2017-1-1
     */

    @Unique
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
                "id='" + id + '\'' +
                "href='" + href + '\'' +
                ", title='" + title + '\'' +
                ", size='" + size + '\'' +
                ", date='" + date + '\'' +
                ", opened='" + opened + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getOpened() {
        return this.opened;
    }
}
