package org.zky.tool.magnetsearch.utils.http;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *            "api_btvda.php?do=get_magnet_info&hash=E4A8FFD932C9EA23D59626C7349CD755B378475D"
 * Created by zhangkun on 2017/3/31.
 */

public interface GetVideoSerivce {

    @GET("api_btvda.php")
    rx.Observable<GetVideoResponseState<List<VideoDataEntity>>> getMagnetInfo(@Query("do") String var1,@Query("hash") String var2 );

    @GET("api_btvda.php")
    rx.Observable<GetVideoResponseState<VideoDataEntity>> parseXFMagnet(@Query("do") String var1,@Query("data") String var2 );
}
