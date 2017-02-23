package org.zky.tool.magnetsearch.introduction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.utils.GetRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkywalker on 2017/1/2.
 * package:org.zky.zky.widget.Indicator
 */

public class IndicatorControllerImpl implements IndicatorController {

    private LinearLayout view;

    private Context context;

    private List<ImageView> list;

    private int count;

    @Override
    public View newInstance(@NonNull Context context) {
        this.context = context;
        return view = (LinearLayout) View.inflate(context, R.layout.indicator_controller,null);
    }

    @Override
    public void initialize(int count) {
        this.count = count;
        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(GetRes.getDrawable(R.drawable.indicator_dot_grey));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.addView(imageView,layoutParams);
            list.add(imageView);
        }
        selectPosition(0);
    }

    @Override
    public void selectPosition(int position) {
        for (int i = 0; i < count; i++) {
            int id = (i==position)?R.drawable.indicator_dot_white:R.drawable.indicator_dot_grey;
            Drawable drawable = GetRes.getDrawable(id);
            list.get(i).setImageDrawable(drawable);
        }
    }
}
