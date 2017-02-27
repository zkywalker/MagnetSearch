package org.zky.tool.magnetsearch;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.zky.tool.magnetsearch.search.SearchAdapter;
import org.zky.tool.magnetsearch.search.SearchEntity;
import org.zky.tool.magnetsearch.utils.GetRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseThemeActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;

    private List<SearchEntity> list = new ArrayList<>();
    private SearchAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        List<SearchEntity> searchEntities = MagnetSearchApp.getInstanse().getDaoSession().getSearchEntityDao().loadAll();
        if (searchEntities.size() > 0)
            adapter.addDatas(searchEntities);
    }

    private void initView() {
        toolbar.setTitle(GetRes.getString(R.string.history_activity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvHistory.setAdapter(adapter = new SearchAdapter(this, list, R.layout.item_recycler_view));
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }
}
