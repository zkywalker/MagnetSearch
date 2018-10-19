package org.zky.tool.magnetsearch.search;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.zky.tool.magnetsearch.base.BasePresenter;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.network.RetrofitClient;

import java.util.List;

import rx.Subscriber;

public class SearchPresenter extends BasePresenter<ISearchUI>{

    public FirebaseAnalytics analytics;

    public int currentPage =0;

    public String currentKeyword;

    public void query(String key, final int page) {
        currentKeyword = key;
        RetrofitClient.getInstance().getData(new Subscriber<List<SearchEntity>>() {
            @Override
            public void onStart() {
                //page=1时候加载显示progress bar
                if (page == 1) {
                    getUI().ClearSearchData();
                    getUI().showLoadingView();
                    getUI().clickMenuIcon();
                }

            }

            @Override
            public void onCompleted() {
                currentPage = page;
                getUI().dismissLoadingView();
                getUI().setListVisibility(true);
            }

            @Override
            public void onError(Throwable e) {
                getUI().dismissLoadingView();
                if (e instanceof IndexOutOfBoundsException) {
                    getUI().snack(R.string.no_data);
                } else{
                    getUI().snack(e.getMessage());
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(List<SearchEntity> searchEntities) {
                if (searchEntities !=null && searchEntities.size()>0){
                    getUI().loadSearchData(searchEntities);
                }
            }
        }, key, page);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, key);
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    @Override
    public void onUICreate(Bundle savedInstanceState) {
        super.onUICreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(getContext());

    }

    public void queryMore() {
        query(currentKeyword, currentPage + 1);
    }
}
