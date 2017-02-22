package org.zky.tool.magnetsearch.search;

import java.util.List;
import java.util.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 * Created by zhangkun on 2017/2/21.
 */

public interface SearchSerivce {

    @GET("{keyword}")
    rx.Observable<List<SearchEntity>> getHtml(@Path("keyword") String keyword);


}
