package org.zky.tool.magnetsearch.search.factory;

import org.zky.tool.magnetsearch.search.SearchEntity;

import java.util.List;

/**
 * 搜索源 接口
 * Created by zhangkun on 2017/4/8.
 */

public interface SearchSource {

    List<SearchEntity> parse(String html);

    String getBaseUrl();

    String getName();

    /**
     * 获取restful url页
     * @param page 第page页
     * @return url页
     */
    String getPage(int page);

}
