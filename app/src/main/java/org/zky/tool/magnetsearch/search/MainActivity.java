package org.zky.tool.magnetsearch.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.zky.tool.magnetsearch.BaseThemeActivity;
import org.zky.tool.magnetsearch.FavoriteActivity;
import org.zky.tool.magnetsearch.HistoryActivity;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.SettingsActivity;
import org.zky.tool.magnetsearch.constants.UrlConstants;
import org.zky.tool.magnetsearch.utils.AnimUtils;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.MessageUtils;
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter;

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

public class MainActivity extends BaseThemeActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";


    private FirebaseAnalytics analytics;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;

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
    @BindView(R.id.et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;


    private Retrofit retrofit;

    private MyAdapter<SearchEntity> adapter;

    private List<SearchEntity> list = new ArrayList<>();

    private int lastRecyclerItem;

    private static final String CURRENT_KEYWORD = "currentKeyword";

    private static final String CURRENT_PAGE = "currentPage";


    private String currentKeyword;

    private int currentPage;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(CURRENT_KEYWORD, currentKeyword);
        outState.putInt(CURRENT_PAGE, currentPage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        analytics = FirebaseAnalytics.getInstance(this);


        if (savedInstanceState != null) {
            currentKeyword = savedInstanceState.getString(CURRENT_KEYWORD);
            currentPage = savedInstanceState.getInt(CURRENT_PAGE, 1);
        }

        CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (!TextUtils.isEmpty(text)){
            etSearch.setText(text);
            etSearch.requestFocus();
        }

    }


    private void initView() {
        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        ivMenu.setOnClickListener(this);
        ivDelete.setOnClickListener(this);

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AnimUtils.zoomIn(ivDelete);
                    ivMenu.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    AnimUtils.zoomIn(ivMenu);

                } else {
                    AnimUtils.zoomOut(ivDelete);
                    GetRes.hideSoftKeyboard(etSearch);
                }

            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String keyword = v.getText().toString();
                    if (validate(keyword))
                        query(keyword, 1);
                    else
                        ivMenu.callOnClick();
                }
                return true;
            }
        });


        recyclerView.setAdapter(adapter = new SearchAdapter(this, list, R.layout.item_recycler_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastRecyclerItem + 1 == adapter.getItemCount()) {
                    query(currentKeyword, currentPage + 1);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastRecyclerItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });

    }

    private boolean validate(String key) {
        if (TextUtils.isEmpty(key)) {
            MessageUtils.snack(findViewById(R.id.activity_content), R.string.keyword_empty);
            return false;
        }
        if (key.contains("/")) {
            MessageUtils.snack(findViewById(R.id.activity_content), R.string.invalid_keyword);
            return false;
        }
        return true;
    }

    private void query(String key, final int page) {
        currentKeyword = key;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getClient())
                    .baseUrl(UrlConstants.BTSO_SEARCH_URL)
                    .addConverterFactory(SearchConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        if (page > 1)
            key = key + "/page/" + page;
        Observable<List<SearchEntity>> observable = retrofit.create(SearchSerivce.class).getHtml(key);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<SearchEntity>>() {
                    @Override
                    public void onStart() {
                        //page=1时候加载显示progress bar
                        if (page == 1) {
                            list.clear();
                            adapter.setCurrentItemCount(0);
                            adapter.notifyDataSetChanged();
                            pbLoading.setVisibility(View.VISIBLE);
                            ivMenu.callOnClick();
                        }

                    }

                    @Override
                    public void onCompleted() {
                        currentPage = page;
                        pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbLoading.setVisibility(View.GONE);
                        if (e instanceof IndexOutOfBoundsException) {
                            Snackbar.make(findViewById(R.id.activity_content), GetRes.getString(R.string.no_data), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        } else

                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<SearchEntity> searchEntities) {
                        Log.i(TAG, "onNext: data\n" + searchEntities);
                        adapter.addDatas(searchEntities);
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, key);
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId) {
            case R.id.nav_favorites:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); //纯文本
                intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.share));
                intent.putExtra(Intent.EXTRA_TEXT, GetRes.getString(R.string.share_content));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, GetRes.getString(R.string.share)));

                break;
            case R.id.nav_send:
                try {
                    intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + GetRes.getString(R.string.email)));
                    intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.email_subject));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    Snackbar.make(findViewById(R.id.activity_content), GetRes.getString(R.string.no_email_app), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();

                }

                break;

        }
        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                etSearch.setText("");
                break;
            case R.id.iv_menu:
                if (etSearch.isFocused()) {
                    ivMenu.setImageResource(R.drawable.ic_menu_black_24dp);
                    AnimUtils.zoomIn(ivMenu);
                    recyclerView.requestFocus();
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }

                break;

        }
    }
}
