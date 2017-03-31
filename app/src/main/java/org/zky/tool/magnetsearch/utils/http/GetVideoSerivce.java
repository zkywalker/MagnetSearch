package org.zky.tool.magnetsearch.utils.http;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * Created by zhangkun on 2017/3/31.
 */

public interface GetVideoSerivce {

    @GET("api.php")
    rx.Observable<GetVideoResponseState<List<VideoDataEntity>>> getMagnetInfo(@Query("do") String var1,@Query("hash") String var2,@Query("s") String var3 );

    @GET("api.php")
    rx.Observable<GetVideoResponseState<VideoDataEntity>> parseXFMagnet(@Query("do") String var1,@Query("data") String var2,@Query("s") String var3 );
}
