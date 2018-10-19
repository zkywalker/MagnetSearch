package org.zky.tool.magnetsearch.base;

import android.content.Context;
import android.os.Bundle;

import org.zky.tool.magnetsearch.base.IPresenter;
import org.zky.tool.magnetsearch.base.IUI;

public abstract class BasePresenter<U extends IUI> implements IPresenter {

    private U mUI;
    private Context mContext;

    public BasePresenter() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IUI> void init(Context context, T ui) {
        this.mContext = context;
        this.mUI = (U) ui;
    }

    protected final U getUI() {
        return mUI;
    }

    protected final Context getContext() {
        return mContext;
    }

    /**
     * 仅为解决内存泄漏添加
     */
    protected final void setContextResNull() {
        mContext = null;
        mUI = null;
    }

    @Override
    public void onUICreate(Bundle savedInstanceState) {

    }

    @Override
    public void onUIStart() {

    }

    @Override
    public void onUIResume() {

    }

    @Override
    public void onUIPause() {

    }

    @Override
    public void onUIStop() {

    }

    @Override
    public void onUIDestory() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    protected String getString(int id){
        return mContext.getString(id);
    }

}
