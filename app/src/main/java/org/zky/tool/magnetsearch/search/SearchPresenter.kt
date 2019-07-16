package org.zky.tool.magnetsearch.search

import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics

import org.zky.tool.magnetsearch.base.BasePresenter
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.network.RetrofitClient

import rx.Subscriber

class SearchPresenter : BasePresenter<ISearchUI>() {

    private lateinit var analytics: FirebaseAnalytics

    var currentPage = 0

    var currentKeyword: String = ""

    fun query(key: String, page: Int) {
        currentKeyword = key
        RetrofitClient.getInstance().getData(object : Subscriber<List<SearchEntity>>() {
            override fun onStart() {
                //page=1时候加载显示progress bar
                if (page == 1) {
                    with(ui) {
                        ClearSearchData()
                        showLoadingView()
                        clickMenuIcon()
                    }
                }

            }

            override fun onCompleted() {
                currentPage = page
                ui.dismissLoadingView()
                ui.setListVisibility(true)
            }

            override fun onError(e: Throwable) {
                ui.dismissLoadingView()
                if (e is IndexOutOfBoundsException) {
                    ui.snack(R.string.no_data)
                } else {
                    ui.snack(e.message)
                }
                e.printStackTrace()
            }

            override fun onNext(searchEntities: List<SearchEntity>?) {
                if (searchEntities != null && searchEntities.isNotEmpty()) {
                    ui.loadSearchData(searchEntities)
                }
            }
        }, key, page)

        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, Bundle().apply { putString(FirebaseAnalytics.Param.SEARCH_TERM, key) })

    }

    override fun onUICreate(savedInstanceState: Bundle) {
        super.onUICreate(savedInstanceState)
        analytics = FirebaseAnalytics.getInstance(context)
    }

    fun queryMore() {
        query(currentKeyword, currentPage + 1)
    }
}
