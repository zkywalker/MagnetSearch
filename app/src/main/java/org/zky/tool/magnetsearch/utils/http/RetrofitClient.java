package org.zky.tool.magnetsearch.utils.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.search.SearchConverterFactory;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.search.SearchService;
import org.zky.tool.magnetsearch.search.factory.BtsoSearchSource;
import org.zky.tool.magnetsearch.search.factory.SearchSource;
import org.zky.tool.magnetsearch.search.factory.SearchSourceFactory;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.http.exception.ApiException;
import org.zky.tool.magnetsearch.utils.http.exception.ExceptionEngine;

import java.io.IOException;
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

    private static SearchSource mSearchSource;

    private static String mCurrentSearchSource = "";

    private Retrofit retrofit;

    private Retrofit getVideoRetrofit;


    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(getClient())
                .baseUrl(mSearchSource.getBaseUrl())
                .addConverterFactory(SearchConverterFactory.create(mSearchSource))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        getVideoRetrofit = new Retrofit.Builder()
                .client(getClient2())
                .baseUrl(UrlConstants.GET_XF_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String source = preferences.getString(GetRes.getString(R.string.key_search_source), BtsoSearchSource.class.getName());

        if (source != mCurrentSearchSource) {
            mCurrentSearchSource = source;
            mSearchSource = SearchSourceFactory.getInstance(source);
            instance = new RetrofitClient();
        }

        return instance;
    }

    /**
     * 搜索 数据
     * @param subscriber 订阅 数据 者
     * @param key 关键词
     */
    public void getData(Subscriber<List<SearchEntity>> subscriber, String key) {
        Observable<List<SearchEntity>> observable = retrofit.create(SearchService.class)
                .getHtml(key,mSearchSource.getPage(1))
                .onErrorResumeNext(new ErrorFunc<List<SearchEntity>>());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    /**
     * 搜索 数据
     * @param subscriber 订阅 数据 者
     * @param key 关键词
     * @param page 分页
     */
    public void getData(Subscriber<List<SearchEntity>> subscriber, String key,int page) {
        Observable<List<SearchEntity>> observable = retrofit.create(SearchService.class)
                .getHtml(key,mSearchSource.getPage(page))
                .onErrorResumeNext(new ErrorFunc<List<SearchEntity>>());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();
    }

    public static OkHttpClient getClient2() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Host", "www.51gdj.com")
                                .addHeader("Connection","Keep-Alive")
                                .addHeader("Accept-Encoding", "gzip")
                                .addHeader("User-Agent", "okhttp/3.6.0")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();
    }

    private class ErrorFunc<T> implements Func1<Throwable,Observable<T>>{

        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }

//    private class HttpResultFunc<T> implements Func1<GetVideoResponseState<T>, T> {
//
//        @Override
//        public T call(GetVideoResponseState<T> httpResult) {
//            if (httpResult.getCode() != 1) {
//                throw new ApiException(httpResult.getMsg());
//            }
//            return httpResult.getData();
//        }
//    }
}
