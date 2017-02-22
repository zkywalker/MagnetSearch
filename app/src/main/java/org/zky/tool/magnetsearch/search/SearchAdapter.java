package org.zky.tool.magnetsearch.search;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter;
import org.zky.tool.magnetsearch.utils.recycler.ViewHolder;

import java.util.List;

/**
 * search adapter
 * Created by zhangkun on 2017/2/22.
 */

public class SearchAdapter extends MyAdapter<SearchEntity> {
    private Context mContext;

    public SearchAdapter(Context context, List<SearchEntity> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder var1, SearchEntity var2) {
        String[] split = var2.getHref().split("/");
        String hash = split[split.length - 1];
        final String magnet = "magnet:?xt=urn:btih:" + hash;
        var1.setText(R.id.tv_magnet, "Distributed Hash Table:" + hash);
        var1.setText(R.id.tv_title, var2.getTitle().trim());
        var1.setText(R.id.tv_size, var2.getSize());
        var1.setText(R.id.tv_date, var2.getDate());

        var1.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = GetRes.getString(R.string.add_to_clipboard);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(magnet));
                    intent.addCategory("android.intent.category.DEFAULT");
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    if (e instanceof ActivityNotFoundException) {
                        message = GetRes.getString(R.string.activity_not_found_error)+","+message;
                    }
                    e.printStackTrace();
                } finally {
                    GetRes.setClipboard(magnet);
                    snack(message);
//                    Toast.makeText(mContext, GetRes.getString(R.string.add_to_clipboard), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void snack(String s){
        Snackbar.make(((Activity)mContext).findViewById(R.id.activity_main), s, Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }
}
