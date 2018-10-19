package org.zky.tool.magnetsearch.settings;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.base.BaseThemeActivity;
import org.zky.tool.magnetsearch.utils.GetRes;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseThemeActivity<SettingsPresenter> implements ISettingsUI {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;

    private FirebaseAnalytics analytics;


    @Override
    public void onCreateExecute(Bundle savedInstanceState) {
        super.onCreateExecute(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar.setTitle(GetRes.getString(R.string.action_settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //加载广告是个耗时操作，延迟加载提高用户体验
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdRequest request = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
                adView.loadAd(request);
            }
        },1000);


        analytics = FirebaseAnalytics.getInstance(this);
        analytics.logEvent("sittings",new Bundle());
    }

    @Override
    protected SettingsPresenter createPresenter() {
        return new SettingsPresenter();
    }


}