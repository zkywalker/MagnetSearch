package org.zky.tool.magnetsearch.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.search.MainActivity;


/**
 * Created by zkywalker on 2017/1/3.
 * package:org.zky.zky.activities
 */

public class DefaultIntroActivity extends IntroductionActivity {
    @Override
    void init(Bundle saveInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.introduction_1));
        addSlide(SampleSlide.newInstance(R.layout.introduction_2));
        addSlide(SampleSlide.newInstance(R.layout.introduction_3));
        addSlide(SampleSlide.newInstance(R.layout.introduction_4));

//        setShowButton(false);
        //自定义indicator，比如说丢进去个进度条什么的
//        setCustomIndicator(new IndicatorController() {
//            private TextView textView;
//            private int slideCount;
//
//            @Override
//            public View newInstance(@NonNull Context context) {
//                textView = new TextView(context);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                textView.setTextColor(Color.WHITE);
//                textView.setGravity(Gravity.CENTER);
//                return textView;
//            }
//
//            @Override
//            public void initialize(int count) {
//                slideCount = count;
//                selectPosition(0);
//            }
//
//            @Override
//            public void selectPosition(int position) {
//                textView.setText(String.format("%d/%d", position + 1, slideCount));
//            }
//        });
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
