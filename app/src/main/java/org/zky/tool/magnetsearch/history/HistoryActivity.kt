package org.zky.tool.magnetsearch.history

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar

import org.greenrobot.greendao.query.QueryBuilder
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.base.BaseThemeActivity
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao
import org.zky.tool.magnetsearch.search.SearchAdapter
import org.zky.tool.magnetsearch.search.SearchEntity
import org.zky.tool.magnetsearch.utils.GetRes
import org.zky.tool.magnetsearch.utils.GreenDaoManager

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseThemeActivity<HistoryPresenter>(), IHistoryUI {

    private val list = ArrayList<SearchEntity>()

    private var adapter: SearchAdapter? = null

    override fun onCreateExecute(savedInstanceState: Bundle?) {
        super.onCreateExecute(savedInstanceState)
        setContentView(R.layout.activity_history)
        initView()
        loadOpened()
    }

    override fun createPresenter(): HistoryPresenter {
        return HistoryPresenter()
    }

    private fun loadAllData() {
        val searchEntities = GreenDaoManager.getInstance().daoSession.searchEntityDao.loadAll()
        if (searchEntities != null)
            adapter!!.datas = searchEntities
    }

    private fun loadOpened() {
        val searchEntityQueryBuilder = GreenDaoManager.getInstance().daoSession.searchEntityDao.queryBuilder()
        val list = searchEntityQueryBuilder.where(SearchEntityDao.Properties.Opened.eq(true)).list()
        if (list != null)
            adapter!!.datas = list
    }

    private fun initView() {
        toolbar!!.title = GetRes.getString(R.string.history_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter = SearchAdapter(this, list, R.layout.item_recycler_view)
        rv_history!!.adapter = adapter
        rv_history!!.layoutManager = LinearLayoutManager(this)

        val tab = tabLayout!!.newTab()
        tab.text = GetRes.getString(R.string.opened)
        tab.tag = GetRes.getString(R.string.opened)
        val tab2 = tabLayout!!.newTab()
        tab2.text = GetRes.getString(R.string.all)
        tab2.tag = GetRes.getString(R.string.all)
        tabLayout!!.addTab(tab)
        tabLayout!!.addTab(tab2)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.tag == GetRes.getString(R.string.opened)) {
                    loadOpened()
                } else if (tab.tag == GetRes.getString(R.string.all)) {
                    loadAllData()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tab.select()
    }

    companion object {
        private val TAG = "HistoryActivity"
    }
}
