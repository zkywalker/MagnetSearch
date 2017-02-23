package org.zky.tool.magnetsearch.introduction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.search.MainActivity;
import org.zky.tool.magnetsearch.utils.GetRes;


/**
 * Created by zkywalker on 2017/1/3.
 * package:org.zky.zky.activities
 */

public class DefaultIntroActivity extends IntroductionActivity {
    @Override
    void onIntroCreate() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String key = GetRes.getString(R.string.key_intro);
        boolean showIntro = sp.getBoolean(key, true);
        if (!showIntro) {
            loadMainActivity();
        }else{
            sp.edit().putBoolean(key, false).apply();
        }

    }

    @Override
    void init(Bundle saveInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.introduction_1));
        addSlide(SampleSlide.newInstance(R.layout.introduction_2));
        addSlide(SampleSlide.newInstance(R.layout.introduction_3));
        addSlide(SampleSlide.newInstance(R.layout.introduction_4));

    }

    @Override
    void onSkip() {
    loadMainActivity();
    }

    @Override
    void onDone() {
    loadMainActivity();
    }



    public void getStarted(View v) {
        loadMainActivity();
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
