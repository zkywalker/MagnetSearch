package org.zky.tool.magnetsearch.settings

import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_settings.*
import org.zky.tool.magnetsearch.R
import org.zky.tool.magnetsearch.base.BaseThemeActivity
import org.zky.tool.magnetsearch.utils.GetRes

class SettingsActivity : BaseThemeActivity<SettingsPresenter>(), ISettingsUI {

    override fun onCreateExecute(savedInstanceState: Bundle?) {
        super.onCreateExecute(savedInstanceState)

        setContentView(R.layout.activity_settings)
        toolbar.title = GetRes.getString(R.string.action_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //加载广告是个耗时操作，延迟加载提高用户体验
        toolbar.postDelayed({
            adView.loadAd(AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build())
        }, 1000)


        FirebaseAnalytics.getInstance(this).logEvent("sittings", Bundle())
    }

    override fun createPresenter(): SettingsPresenter {
        return SettingsPresenter()
    }

    companion object {
        private val TAG = "SettingsActivity"
    }

}