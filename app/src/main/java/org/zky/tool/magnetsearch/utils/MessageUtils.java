package org.zky.tool.magnetsearch.utils;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.zky.tool.magnetsearch.R;

/**
 *
 * Created by zhangkun on 2017/2/23.
 */

public class MessageUtils {

    public static final String TAG = "ToastUtils";
    public static boolean isShow = true;

    private static Handler mHandler;
    private static Toast mToast;
    private static Context mContext;

    private static long lastToastTime = 0;

    public static void init(Context context) {
        isShow = true;
        mContext = context;
        mHandler = new Handler();
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void dismissToast() {
        isShow = false;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showToastShort(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showToastLong(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showSnackShort(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showSnackLong(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static void showToastOnce(String stringId) {
        //短期内不能再吐司了
        if (System.currentTimeMillis()- lastToastTime >5000){
            ToastUtils.showToastShort(stringId);
            lastToastTime = System.currentTimeMillis();
        }
    }

    public static void showToastOnceInDuration(String stringId, long duration) {
        //短期内不能再吐司了
        if (System.currentTimeMillis()- lastToastTime >duration){
            ToastUtils.showToastShort(stringId);
            lastToastTime = System.currentTimeMillis();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void showToastDuration(final String message, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    Toast.makeText(mContext, message, duration).show();
                }
            }
        });

    }
    /**
     * 居中显示显示Toast
     *
     * @param message
     */
    public static void showToastOnCenter(final String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    mToast.setText(message);
                    mToast.setGravity(Gravity.CENTER,0,0);
                    mToast.show();
                }
            }
        });
    }

    /**
     * 自定义两行的吐丝
     */
    public static void showToastDoubleLine(final String firstLineMessage, final String secondLineMessage){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow){
                    Toast result = new Toast(mContext);
                    LayoutInflater inflate = (LayoutInflater)
                            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (inflate == null){
                        return;
                    }
                    View v = inflate.inflate(R.layout.toast_second_line, null);
                    TextView firstLineTv = (TextView) v.findViewById(R.id.first_line_tv);
                    firstLineTv.setText(firstLineMessage);
                    TextView secondLineTv = (TextView) v.findViewById(R.id.second_line_tv);
                    secondLineTv.setText(secondLineMessage);
                    result.setDuration(Toast.LENGTH_SHORT);
                    result.setView(v);
                    result.show();
                }
            }
        });
    }

    public static void snack(final View view, @StringRes final int message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, GetRes.getString(message), Snackbar.LENGTH_LONG).setAction(GetRes.getString(R.string.i_know), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    public static void snack(final View view, final String message, final String buttonText, final View.OnClickListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG).setAction(buttonText, listener).show();
            }
        });
    }
}
