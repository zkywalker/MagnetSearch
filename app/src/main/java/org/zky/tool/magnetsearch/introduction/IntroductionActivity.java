package org.zky.tool.magnetsearch.introduction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import org.zky.tool.magnetsearch.R;

import java.util.ArrayList;
import java.util.List;

/**
 *介绍页面的基类
 * */
public abstract class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vp_slide;

    private PagerAdapter adapter;

    private FrameLayout container;

    private List<Fragment> fragments = new ArrayList<>();

    private int slideCount;

    private IndicatorController controller;

    private boolean showButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //请求窗口特色没有actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_introduction);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        final Button btn_skip = (Button) findViewById(R.id.btn_skip);
        final Button btn_done = (Button) findViewById(R.id.btn_done);
        final ImageButton btn_next = (ImageButton) findViewById(R.id.ib_next);
        container = (FrameLayout) findViewById(R.id.fl_indicator_container);

        btn_skip.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        vp_slide = (ViewPager) findViewById(R.id.vp_slide);
        adapter = new PagerAdapter(this.getSupportFragmentManager(), fragments);
        vp_slide.setAdapter(adapter);
        vp_slide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (slideCount > 1)
                    controller.selectPosition(position);
                //当滑到最后一页时
                if (showButton) {
                    if (position == slideCount - 1) {
                        btn_skip.setVisibility(View.GONE);
                        btn_next.setVisibility(View.GONE);
                        btn_done.setVisibility(View.VISIBLE);
                    } else {
                        btn_skip.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                        btn_done.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //init里添加slide
        init(savedInstanceState);
        slideCount = fragments.size();
        //只有一个slide时
        if (slideCount == 1 && showButton) {
            btn_next.setVisibility(View.GONE);
            btn_done.setVisibility(View.VISIBLE);
            btn_skip.setVisibility(View.GONE);
        } else {
            initIndicator();
        }

        //是否显示按钮
        if (showButton) {
            btn_next.setVisibility(View.VISIBLE);
            btn_done.setVisibility(View.GONE);
            btn_skip.setVisibility(View.VISIBLE);
        } else {
            btn_next.setVisibility(View.GONE);
            btn_done.setVisibility(View.GONE);
            btn_skip.setVisibility(View.GONE);
        }


    }

    private void initIndicator() {
        if (controller == null) {
            controller = new IndicatorControllerImpl();
        }
        container.addView(controller.newInstance(this));
        controller.initialize(slideCount);
    }

    ;

    public void setCustomIndicator(IndicatorController controller) {
        this.controller = controller;
    }

    public void setShowButton(boolean show) {
        showButton = show;
    }

    public void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        adapter.notifyDataSetChanged();
    }

    abstract void init(Bundle saveInstanceState);

    abstract void onSkip();

    abstract void onDone();


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                onSkip();
                break;
            case R.id.btn_done:
                onDone();
                break;
            case R.id.ib_next:
                //圆滑切换
                vp_slide.setCurrentItem(vp_slide.getCurrentItem() + 1, true);
                break;
        }
    }
}
