package org.zky.tool.magnetsearch.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;

import org.zky.tool.magnetsearch.base.BaseThemeActivity;
import org.zky.tool.magnetsearch.history.FavoriteActivity;
import org.zky.tool.magnetsearch.history.HistoryActivity;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.settings.SettingsActivity;
import org.zky.tool.magnetsearch.network.RetrofitClient;
import org.zky.tool.magnetsearch.utils.AnimUtils;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.MessageUtils;
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends BaseThemeActivity<SearchPresenter> implements ISearchUI, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.ftl_search)
    FabTagLayout ftlSearch;
    @BindView(R.id.ftl_sort_size)
    FabTagLayout ftlSortSize;
    @BindView(R.id.ftl_sort_date)
    FabTagLayout ftlSortDate;
    @BindView(R.id.fabPlus)
    FloatingActionButtonPlus fabPlus;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
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

    private MyAdapter<SearchEntity> adapter;

    private int lastRecyclerItem;

    private static final String CURRENT_KEYWORD = "currentKeyword";

    private static final String CURRENT_PAGE = "currentPage";


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (getPresenter()==null){
            return;
        }
        outState.putString(CURRENT_KEYWORD, getPresenter().currentKeyword);
        outState.putInt(CURRENT_PAGE, getPresenter().currentPage);
    }

    @Override
    public void onCreateExecute(Bundle savedInstanceState) {
        super.onCreateExecute(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();


        if (savedInstanceState != null) {
            getPresenter().currentKeyword = savedInstanceState.getString(CURRENT_KEYWORD);
            getPresenter().currentPage = savedInstanceState.getInt(CURRENT_PAGE, 1);
        }

        handleProcessText();
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    private void handleProcessText() {
        CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (!TextUtils.isEmpty(text)) {
            etSearch.setText(text);
            etSearch.requestFocus();
        }
    }


    private void initView() {
        //fab
        fabPlus.setRotateValues(90);
        ftlSearch.setFabOnClickListener(new FabTagLayout.FabOnClickListener() {
            @Override
            public void onClick() {
                snack("search");
            }
        });
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
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String keyword = v.getText().toString();
                    if (validate(keyword))
                        getPresenter().query(keyword, 1);
                    else
                        ivMenu.callOnClick();
                }
                return true;
            }
        });


        recyclerView.setAdapter(adapter = new SearchAdapter(this, new ArrayList<SearchEntity>(), R.layout.item_recycler_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastRecyclerItem + 1 == adapter.getItemCount()) {
                    getPresenter().queryMore();
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
                    snack(R.string.no_email_app);
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

    @Override
    public void showLoadingView() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingView() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void snack(String s) {
        Snackbar.make(findViewById(R.id.activity_content), s, Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    @Override
    public void snack(@StringRes int s) {
        Snackbar.make(findViewById(R.id.activity_content), GetRes.getString(s), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    @Override
    public void loadSearchData(List<SearchEntity> list) {
        if (list == null || list.size() == 0){
            return;
        }
        if (adapter != null){
            adapter.addDatas(list);
        }
    }

    @Override
    public void ClearSearchData() {
        adapter.clearAll();
    }

    @Override
    public void clickMenuIcon() {
        ivMenu.callOnClick();
    }

    @Override
    public void setListVisibility(boolean v) {
        recyclerView.setVisibility(v?View.VISIBLE:View.GONE);
    }
}
