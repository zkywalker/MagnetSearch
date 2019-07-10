package org.zky.tool.magnetsearch.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorite.*
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.base.BaseThemeActivity
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao
import org.zky.tool.magnetsearch.search.SearchAdapter
import org.zky.tool.magnetsearch.search.SearchEntity
import org.zky.tool.magnetsearch.utils.GetRes
import org.zky.tool.magnetsearch.utils.GreenDaoManager
import java.util.*

class FavoriteActivity : BaseThemeActivity<FavoritePresenter>(), IFavoriteUI {

    private val list = ArrayList<SearchEntity>()

    private var adapter: SearchAdapter? = null

    override fun onCreateExecute(savedInstanceState: Bundle?) {
        super.onCreateExecute(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initView()
        query()
    }

    override fun createPresenter(): FavoritePresenter {
        return FavoritePresenter()
    }

    private fun query() {
        val builder = GreenDaoManager.getInstance().daoSession.searchEntityDao.queryBuilder()
        val list = builder.where(SearchEntityDao.Properties.IsFavorite.eq(true)).list()
        adapter!!.datas = list
        srl_refresh!!.isRefreshing = false
    }

    private fun initView() {
        toolbar!!.title = GetRes.getString(R.string.favorites_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter = SearchAdapter(this, list, R.layout.item_recycler_view)
        rv_favorite!!.adapter = adapter
        rv_favorite!!.layoutManager = LinearLayoutManager(this)

        srl_refresh!!.setOnRefreshListener { query() }
    }
}
