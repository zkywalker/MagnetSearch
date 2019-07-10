package org.zky.tool.magnetsearch.search

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StringRes
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.btn.*
import kotlinx.android.synthetic.main.search_layout.*
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.base.BaseThemeActivity
import org.zky.tool.magnetsearch.history.FavoriteActivity
import org.zky.tool.magnetsearch.history.HistoryActivity
import org.zky.tool.magnetsearch.settings.SettingsActivity
import org.zky.tool.magnetsearch.utils.AnimUtils
import org.zky.tool.magnetsearch.utils.GetRes
import org.zky.tool.magnetsearch.utils.MessageUtils
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter
import java.util.*

class MainActivity : BaseThemeActivity<SearchPresenter>(), ISearchUI, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private var adapter: MyAdapter<SearchEntity>? = null

    private var lastRecyclerItem: Int = 0

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (presenter == null) {
            return
        }
        outState.putString(CURRENT_KEYWORD, presenter.currentKeyword)
        outState.putInt(CURRENT_PAGE, presenter.currentPage)
    }

    override fun onCreateExecute(savedInstanceState: Bundle?) {
        super.onCreateExecute(savedInstanceState)

        setContentView(R.layout.activity_main)
        initView()


        if (savedInstanceState != null) {
            presenter.currentKeyword = savedInstanceState.getString(CURRENT_KEYWORD)
            presenter.currentPage = savedInstanceState.getInt(CURRENT_PAGE, 1)
        }

        handleProcessText()
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter()
    }

    private fun handleProcessText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            if (!TextUtils.isEmpty(text)) {
                et_search.setText(text)
//                et_search.requestFocus()
                et_search.postDelayed({
                    presenter.query(text.toString(), 1)
                },200)
            }
        }
    }

    private fun initView() {
        //fab
        fabPlus!!.setRotateValues(90f)
        ftl_search!!.setFabOnClickListener { snack("search") }
        //drawer
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout!!.setDrawerListener(toggle)
        toggle.syncState()
        nav_view!!.setNavigationItemSelectedListener(this)

        iv_menu!!.setOnClickListener(this)
        iv_delete!!.setOnClickListener(this)

        et_search!!.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                AnimUtils.zoomIn(iv_delete)
                iv_menu!!.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                AnimUtils.zoomIn(iv_menu)

            } else {
                AnimUtils.zoomOut(iv_delete)
                GetRes.hideSoftKeyboard(et_search!!)
            }
        }

        et_search!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                    || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {
                val keyword = v.text.toString()
                if (validate(keyword))
                    presenter.query(keyword, 1)
                else
                    iv_menu!!.callOnClick()
            }
            true
        }

        adapter = SearchAdapter(this, ArrayList(), R.layout.item_recycler_view)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastRecyclerItem + 1 == adapter!!.itemCount) {
                    presenter.queryMore()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                lastRecyclerItem = (recycler_view!!.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
        })


    }

    private fun validate(key: String): Boolean {
        if (TextUtils.isEmpty(key)) {
            MessageUtils.snack(findViewById(R.id.activity_content), R.string.keyword_empty)
            return false
        }
        if (key.contains("/")) {
            MessageUtils.snack(findViewById(R.id.activity_content), R.string.invalid_keyword)
            return false
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        val intent: Intent
        when (itemId) {
            R.id.nav_favorites -> {
                intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_history -> {
                intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain" //纯文本
                intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.share))
                intent.putExtra(Intent.EXTRA_TEXT, GetRes.getString(R.string.share_content))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent.createChooser(intent, GetRes.getString(R.string.share)))
            }
            R.id.nav_send -> try {
                intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:" + GetRes.getString(R.string.email))
                intent.putExtra(Intent.EXTRA_SUBJECT, GetRes.getString(R.string.email_subject))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                snack(R.string.no_email_app)
            }

        }
        drawer_layout!!.closeDrawer(Gravity.LEFT)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_delete -> et_search!!.setText("")
            R.id.iv_menu -> if (et_search!!.isFocused) {
                iv_menu!!.setImageResource(R.drawable.ic_menu_black_24dp)
                AnimUtils.zoomIn(iv_menu)
                recycler_view!!.requestFocus()
            } else {
                drawer_layout!!.openDrawer(Gravity.LEFT)
            }
        }
    }

    override fun showLoadingView() {
        pb_loading!!.visibility = View.VISIBLE
    }

    override fun dismissLoadingView() {
        pb_loading!!.visibility = View.GONE
    }

    override fun snack(s: String) {
        Snackbar.make(findViewById(R.id.activity_content), s, Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know)) { }.show()
    }

    override fun snack(@StringRes s: Int) {
        Snackbar.make(findViewById(R.id.activity_content), GetRes.getString(s), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know)) { }.show()
    }

    override fun loadSearchData(list: List<SearchEntity>?) {
        if (list == null || list.size == 0) {
            return
        }
        if (adapter != null) {
            adapter!!.addDatas(list)
        }
    }

    override fun ClearSearchData() {
        adapter!!.clearAll()
    }

    override fun clickMenuIcon() {
        iv_menu!!.callOnClick()
    }

    override fun setListVisibility(v: Boolean) {
        recycler_view!!.visibility = if (v) View.VISIBLE else View.GONE
    }

    companion object {

        private val TAG = "MainActivity"

        private val CURRENT_KEYWORD = "currentKeyword"

        private val CURRENT_PAGE = "currentPage"
    }
}
