package org.zky.tool.magnetsearch.search;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;

import org.zky.tool.magnetsearch.MagnetSearchApp;
import org.zky.tool.magnetsearch.R;
import org.zky.tool.magnetsearch.greendao.gen.SearchEntityDao;
import org.zky.tool.magnetsearch.utils.GetRes;
import org.zky.tool.magnetsearch.utils.QrDialogManager;
import org.zky.tool.magnetsearch.utils.recycler.MyAdapter;
import org.zky.tool.magnetsearch.utils.recycler.ViewHolder;

import java.util.List;


/**
 * search adapter
 * Created by zhangkun on 2017/2/22.
 */

public class SearchAdapter extends MyAdapter<SearchEntity> {
    private static final String TAG = "SearchAdapter";
    private Context mContext;
    private SearchEntityDao searchEntityDao;
    private List<SearchEntity> list;

    private SharedPreferences preferences;

    private QrDialogManager manager;

    public SearchAdapter(Context context, List<SearchEntity> datas, int layoutId) {
        super(context, datas, layoutId, R.layout.recycler_view_footer);
        mContext = context;
        searchEntityDao = MagnetSearchApp.getInstanse().getDaoSession().getSearchEntityDao();
        list = searchEntityDao.loadAll();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        manager = new QrDialogManager(mContext);

    }

    @Override
    public void convert(final ViewHolder var1, final SearchEntity var2, int type) {
        if (type == TYPE_ITEM) {

            String[] split = var2.getHref().split("/");
            final String hash = split[split.length - 1];
            final String magnet = "magnet:?xt=urn:btih:" + hash;
            var1.setText(R.id.tv_magnet, "Hash:" + hash);
            var1.setText(R.id.tv_title, Html.fromHtml(var2.getTitle().trim()));
            var1.setText(R.id.tv_size, var2.getSize());
            var1.setText(R.id.tv_date, var2.getDate());
            setFavorite(var1, var2.getIsFavorite());

            var1.setOnClickListener(R.id.iv_share, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (preferences.getBoolean(GetRes.getString(R.string.key_share_qr), true)) {
                        manager.show();
                        manager.createQR(magnet, var2.getTitle(), hash);
                    } else {
                        manager.shareText(magnet);
                    }
                }
            });

            var1.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = GetRes.getString(R.string.add_to_clipboard);
                    try {
                        if (preferences.getBoolean(GetRes.getString(R.string.key_fast_open), true)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(magnet));
                            intent.addCategory("android.intent.category.DEFAULT");
                            mContext.startActivity(intent);
                        }
                    } catch (Exception e) {
                        if (e instanceof ActivityNotFoundException) {
                            message = GetRes.getString(R.string.activity_not_found_error) + "," + message;
                        }
                        e.printStackTrace();
                    } finally {
                        GetRes.setClipboard(magnet);
                        snack(message);
                        setOpened(var2);
                    }

                }
            });
            var1.setOnClickListener(R.id.iv_favorite, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    var2.setFavorite(!var2.getIsFavorite());
                    List<SearchEntity> list = searchEntityDao.queryBuilder().where(SearchEntityDao.Properties.Title.eq(var2.getTitle())).list();
                    if (list.size() != 0) {
                        list.get(0).setFavorite(var2.getIsFavorite());
                        searchEntityDao.update(list.get(0));
                    }
                    setFavorite(var1, var2.getIsFavorite());
                }
            });

        }


    }


    private void setOpened(SearchEntity searchEntity) {
        List<SearchEntity> list = searchEntityDao.queryBuilder().where(SearchEntityDao.Properties.Title.eq(searchEntity.getTitle())).list();
        if (list.size() > 0) {
            SearchEntity entity = list.get(0);
            entity.setOpened(true);
            searchEntityDao.update(entity);
        }

    }

    private void setFavorite(ViewHolder var1, boolean favorite) {
        if (favorite) {
            var1.setImageResource(R.id.iv_favorite, R.drawable.ic_favorite_red_24dp);
        } else {
            var1.setImageResource(R.id.iv_favorite, R.drawable.ic_favorite_border_black_24dp);

        }
    }

    private void snack(String s) {
        Snackbar.make(((Activity) mContext).findViewById(R.id.activity_content), s, Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }
}
