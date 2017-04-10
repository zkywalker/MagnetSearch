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

}
