package org.zky.tool.magnetsearch.history;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import org.greenrobot.greendao.query.QueryBuilder;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.base.BaseThemeActivity;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.search.SearchAdapter;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.utils.GetRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseThemeActivity<FavoritePresenter> implements IFavoriteUI {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_favorite)
    RecyclerView rvFavorite;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.activity_content)
    RelativeLayout activityContent;

    private List<SearchEntity> list = new ArrayList<>();
    private SearchAdapter adapter;

    @Override
    public void onCreateExecute(Bundle savedInstanceState) {
        super.onCreateExecute(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        initView();
        query();
    }

    @Override
    protected FavoritePresenter createPresenter() {
        return new FavoritePresenter();
    }

    private void query() {
        QueryBuilder<SearchEntity> builder = MagnetSearchApp.getInstanse().getDaoSession().getSearchEntityDao().queryBuilder();
        List<SearchEntity> list = builder.where(SearchEntityDao.Properties.IsFavorite.eq(true)).list();
        adapter.setDatas(list);
        srlRefresh.setRefreshing(false);
    }

    private void initView() {
        toolbar.setTitle(GetRes.getString(R.string.favorites_activity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvFavorite.setAdapter(adapter = new SearchAdapter(this, list, R.layout.item_recycler_view));
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }
}
