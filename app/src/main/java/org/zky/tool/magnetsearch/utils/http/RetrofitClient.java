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
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by zhangkun on 2017/3/16.
 */

public class RetrofitClient {
    private static RetrofitClient instance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .client(getClient())
                .baseUrl(UrlConstants.BTSO_SEARCH_URL)
                .addConverterFactory(SearchConverterFactory.create())
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


    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("host", "btso.pw")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();
    }
}
