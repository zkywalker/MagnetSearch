package org.zky.tool.magnetsearch.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 *
 * Created by zhangkun on 2017/2/27.
 */

public class AnimUtils {

    public static void zoomIn(final View view){
        ObjectAnimator a = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
                .setDuration(300);
        view.setVisibility(View.VISIBLE);
        a.start();
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    view.setScaleY(v);
            }
        });
    }

    public static void zoomOut(final View view){
        ObjectAnimator a = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
                .setDuration(300);
        a.start();
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                view.setScaleY(v);
                if (v==0f)
                    view.setVisibility(View.GONE);
            }
        });
    }
}
