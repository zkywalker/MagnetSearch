package org.zky.tool.magnetsearch.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import org.zky.tool.magnetsearch.BaseThemeActivity;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter;
import org.zky.tool.magnetsearch.utils.recycler.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class MainActivity extends BaseThemeActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.sv_keyword)
    SearchView svKeyword;

    Retrofit retrofit;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private List<SearchEntity> list = new ArrayList<>();

    private MyAdapter<SearchEntity> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        svKeyword.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        recyclerView.setAdapter(adapter = new MyAdapter<SearchEntity>(this, list, R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder var1, SearchEntity var2) {
                String[] split = var2.getHref().split("/");
                String hash = split[split.length - 1];
                final String magnet = "magnet:?xt=urn:btih:" + hash;
                var1.setText(R.id.tv_magnet, "hash:" + hash);
                var1.setText(R.id.tv_title, var2.getTitle().trim());
                var1.setText(R.id.tv_size, var2.getSize());
                var1.setText(R.id.tv_date, var2.getDate());

                var1.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(magnet));
                            intent.addCategory("android.intent.category.DEFAULT");
                            startActivity(intent);
                        } catch (Exception e) {
                            if (e instanceof ActivityNotFoundException) {

                                Toast.makeText(MainActivity.this, GetRes.getString(R.string.activity_not_found_error), Toast.LENGTH_SHORT).show();
                            }
                            e.printStackTrace();
                        } finally {
                            GetRes.setClipboard(magnet);
                            Toast.makeText(MainActivity.this, GetRes.getString(R.string.add_to_clipboard), Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void query(String key) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(genericClient())
                    .baseUrl(UrlConstants.BTSO_SEARCH_URL)
                    .addConverterFactory(SearchConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        Observable<List<SearchEntity>> observable = retrofit.create(SearchSerivce.class).getHtml(key);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<SearchEntity>>() {
                    @Override
                    public void onStart() {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        pbLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbLoading.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<SearchEntity> searchEntities) {
                        Log.i(TAG, "onNext: data\n" + searchEntities);
                        adapter.addDatas(searchEntities);
                    }
                });
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
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

        return httpClient;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
