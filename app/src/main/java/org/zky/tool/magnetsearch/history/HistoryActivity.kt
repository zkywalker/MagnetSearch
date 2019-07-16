package org.zky.tool.magnetsearch.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_history.*
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.base.BaseThemeActivity
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao
import org.zky.tool.magnetsearch.search.SearchAdapter
import org.zky.tool.magnetsearch.search.SearchEntity
import org.zky.tool.magnetsearch.utils.GetRes
import org.zky.tool.magnetsearch.utils.GreenDaoManager
import java.util.*

class HistoryActivity : BaseThemeActivity<HistoryPresenter>(), IHistoryUI {

    private val list = ArrayList<SearchEntity>()

    private var adapter: SearchAdapter? = null

    override fun onCreateExecute(savedInstanceState: Bundle?) {
        super.onCreateExecute(savedInstanceState)
        setContentView(R.layout.activity_history)
        initView()
        loadOpened()
    }

    override fun createPresenter(): HistoryPresenter = HistoryPresenter()

    private fun loadAllData() {
        val searchEntities = GreenDaoManager.getInstance().daoSession.searchEntityDao.loadAll()
        if (searchEntities != null)
            adapter!!.datas = searchEntities
    }

    private fun loadOpened() {
        val searchEntityQueryBuilder = GreenDaoManager.getInstance().daoSession.searchEntityDao.queryBuilder()
        val list = searchEntityQueryBuilder.where(SearchEntityDao.Properties.Opened.eq(true)).list()
        if (list != null)
            adapter?.datas = list
    }

    val opened = GetRes.getString(R.string.opened)

    val all = GetRes.getString(R.string.all)

    private fun initTab(tabText: String) {
        tabLayout.newTab().apply {
            text = tabText
            tag = tabText
            tabLayout.addTab(this)
        }
    }

    private fun initView() {
        toolbar.title = GetRes.getString(R.string.history_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        adapter = SearchAdapter(this, list, R.layout.item_recycler_view)
        rv_history.adapter = adapter
        rv_history.layoutManager = LinearLayoutManager(this)

        initTab(opened)
        initTab(all)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.tag == opened) {
                    loadOpened()
                } else if (tab.tag == all) {
                    loadAllData()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tabLayout.getTabAt(0)?.select()
    }

    companion object {
        private val TAG = "HistoryActivity"
    }
}
