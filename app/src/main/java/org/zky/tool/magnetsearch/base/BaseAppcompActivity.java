package org.zky.tool.magnetsearch.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;


import org.zky.tool.magnetsearch.utils.MessageUtils;
import org.zky.tool.magnetsearch.utils.ToastUtils;


/**
 *
 * Created by zhangkun on 2017/9/15.
 *
 */

public class BaseAppcompActivity extends AppCompatActivity implements IUI {
    protected boolean mIsActivityDestoryed = false;
    private boolean isPaused;
    private boolean isStoped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActivityDestoryed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onPause() {
            super.onPause();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        mIsActivityDestoryed = true;
        super.onDestroy();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void dismissLoadingView() {

    }

    @Override
    public String getPageName() {
        return getClass().getSimpleName();
    }

    /**
     * isActivityDestoryed:Activity是否已经Destory了. <br/>
     *
     * @return true, Activity已经销毁了，不要在执行任何Fragment事务、显示Dialog等操作
     */
    public boolean isActivityDestoryed() {
        return mIsActivityDestoryed;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStoped = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isStoped = false;
    }

    @Override
    protected void onStop() {
        isStoped = true;
        super.onStop();
    }


    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isDestoryed() {
        return isActivityDestoryed();
    }

    @Override
    public boolean isDetached() {
        return isDestoryed();
    }

    @Override
    public boolean isStopped() {
        return isStoped;
    }

    @Override
    public boolean isFragmentHidden() {
        return isDestoryed();
    }

    @Override
    public boolean isVisibleToUser() {
        return !isPaused();
    }

    @Override
    public void showToast(String content) {
        ToastUtils.showToastShort(content);
    }

    @Override
    public void showToast(int stringId) {
        showToast(getString(stringId));
    }

    @Override
    public void showSnack(int stringId) {
        showSnack(getString(stringId));
    }

    @Override
    public void showSnack(String content) {
        MessageUtils.showSnackShort(content);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

//    protected void backToHome() {
//        if (LoginUtil.isLogin() && !App.getInstance().isHomeStarted()) {
//            MainActivity.launcher(this, 0, true);
//        }
//    }

}
