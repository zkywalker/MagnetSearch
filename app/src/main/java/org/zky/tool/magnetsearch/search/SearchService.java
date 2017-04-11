package org.zky.tool.magnetsearch.search;

import java.util.List;
import java.util.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 * Created by zhangkun on 2017/2/21.
 */

public interface SearchService {

    @GET("{keyword}/{page}/")
    rx.Observable<List<SearchEntity>> getHtml(@Path("keyword") String keyword ,@Path("page") String page);


}
