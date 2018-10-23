package org.zky.tool.magnetsearch.history;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.greenrobot.greendao.query.QueryBuilder;
import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.base.BaseThemeActivity;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.search.SearchAdapter;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.GreenDaoManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseThemeActivity<HistoryPresenter> implements IHistoryUI {
    private static final String TAG = "HistoryActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private List<SearchEntity> list = new ArrayList<>();
    private SearchAdapter adapter;

    @Override
    public void onCreateExecute(Bundle savedInstanceState) {
        super.onCreateExecute(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initView();
        loadOpened();
    }

    @Override
    protected HistoryPresenter createPresenter() {
        return new HistoryPresenter();
    }

    private void loadAllData() {
        List<SearchEntity> searchEntities = GreenDaoManager.getInstance().getDaoSession().getSearchEntityDao().loadAll();
        if (searchEntities!= null)
            adapter.setDatas(searchEntities);
    }

    private void loadOpened(){
        QueryBuilder<SearchEntity> searchEntityQueryBuilder = GreenDaoManager.getInstance().getDaoSession().getSearchEntityDao().queryBuilder();
        List<SearchEntity> list = searchEntityQueryBuilder.where(SearchEntityDao.Properties.Opened.eq(true)).list();
        if (list!=null)
            adapter.setDatas(list);
    }

    private void initView() {
        toolbar.setTitle(GetRes.getString(R.string.history_activity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvHistory.setAdapter(adapter = new SearchAdapter(this, list, R.layout.item_recycler_view));
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(GetRes.getString(R.string.opened));
        tab.setTag(GetRes.getString(R.string.opened));
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText(GetRes.getString(R.string.all));
        tab2.setTag(GetRes.getString(R.string.all));
        tabLayout.addTab(tab);
        tabLayout.addTab(tab2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag().equals(GetRes.getString(R.string.opened))){
                    loadOpened();
                }else if (tab.getTag().equals(GetRes.getString(R.string.all))){
                    loadAllData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.select();
    }
}
