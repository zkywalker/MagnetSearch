package org.zky.tool.magnetsearch.utils.http;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.search.MainActivity;
import org.zky.tool.magnetsearch.search.SearchConverterFactory;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.search.SearchSerivce;
import org.zky.tool.magnetsearch.utils.GetRes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * TODO 这个需要改
 * Created by zhangkun on 2017/3/16.
 */

public class RetrofitClient {
    private static RetrofitClient instance;

    private Retrofit retrofit;

    private Retrofit getVideoRetrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .client(getClient())
                .baseUrl(UrlConstants.BTSO_SEARCH_URL)
                .addConverterFactory(SearchConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        getVideoRetrofit =new Retrofit.Builder()
                .client(getClient())
                .baseUrl(UrlConstants.GET_XF_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance(){
        if (instance ==null)
            instance =new RetrofitClient();
        return instance;
    }

    public void getData(Subscriber<List<SearchEntity>> subscriber,String key){
        Observable<List<SearchEntity>> observable = retrofit.create(SearchSerivce.class).getHtml(key);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    //?do=get_magnet_info&hash={hash}&s=d4da0ccb451650405903afcab318bd55
    public void getMagnetInfo(Subscriber<List<VideoDataEntity>> subscriber, String hash) {
        getVideoRetrofit.create(GetVideoSerivce.class).getMagnetInfo("get_magnet_info",hash
//                ,"d4da0ccb451650405903afcab318bd55"
        )
                .map(new HttpResultFunc<List<VideoDataEntity>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //api.php?do=parse_xf_magnet&data={data}&s=2f8ac6532a4a07e3d68043d536ff2bfb
    public void parseXFMagnet(Subscriber<VideoDataEntity> subscriber, String data) {
        getVideoRetrofit.create(GetVideoSerivce.class).parseXFMagnet("parse_xf_magnet",data
//                , "d4da0ccb451650405903afcab318bd55"
        )
                .map(new HttpResultFunc<VideoDataEntity>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("host", "btso.pw")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();
    }

    private class HttpResultFunc<T> implements Func1<GetVideoResponseState<T>, T> {

        @Override
        public T call(GetVideoResponseState<T> httpResult) {
            //TODO 修改
            Log.i("network", "call: httpResult:"+httpResult.toString());
            if (httpResult.getCode() != 1) {
                throw new ApiException(httpResult.getMsg());
            }
            return httpResult.getData();
        }
    }
}
