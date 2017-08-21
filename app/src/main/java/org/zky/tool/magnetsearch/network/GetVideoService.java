package org.zky.tool.magnetsearch.network;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * http://www.51gdj.com/jiexi/yunbo/api_yunbo.php?do=parse_xf_magnet&data=303264623630623938366631343762656431343831663037613731383432323165663862316664652c322c313635373636
 * Created by zhangkun on 2017/3/31.
 */

public interface GetVideoService {

    @GET("api_yunbo.php")
    rx.Observable<GetVideoResponseState<List<VideoDataEntity>>> getMagnetInfo(@Query("do") String var1, @Query("hash") String var2);

    @GET("api_yunbo.php")
    rx.Observable<GetVideoResponseState<VideoDataEntity>> parseXFMagnet(@Query("do") String var1, @Query("data") String var2);
}
