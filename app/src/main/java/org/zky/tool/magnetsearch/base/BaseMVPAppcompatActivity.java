package org.zky.tool.magnetsearch.base;

import android.os.Bundle;

public abstract class BaseMVPAppcompatActivity<P extends IPresenter> extends BaseAppcompActivity{
    public static final String KEY_DATA = "keyDataOfActivity";

    protected P mPresenter;

    protected Bundle mBundle;

    protected void beforeSetContentView(){}

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        if (savedInstanceState != null) {
            if (savedInstanceState.getBundle(KEY_DATA) != null) {
                mBundle = savedInstanceState.getBundle(KEY_DATA);
            }
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            mBundle = getIntent().getExtras();
        }

        this.mPresenter = createPresenter();
        getPresenter().init(BaseMVPAppcompatActivity.this, getUI());
        onCreateExecute(savedInstanceState);
        getPresenter().onUICreate(savedInstanceState);
    }

    /**
     * onCreateExecute:所有BaseMVPActivity的子类不能再实现onCreate()方法，而是实现onCreateExecute()方法. <br/>
     *
     * @param savedInstanceState
     */
    protected abstract void onCreateExecute(Bundle savedInstanceState);

    /**
     * createPresenter:创建一个Presenter，子类来实现，可以通过new的方式直接new出来一个. <br/>
     *
     * @return
     */
    protected abstract P createPresenter();

    /**
     * getUI:得到UI层组件，一般都是Activity或者Fragment本身. <br/>
     *
     * @return
     */
    protected abstract IUI getUI();

    /**
     * getPresenter:子类应该通过这个方法拿到Presenter的实例，而不是通过变量拿到. <br/>
     *
     * @return Presenter
     */
    protected final P getPresenter() {
        return mPresenter;
    }


    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onUIStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onUIResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onUIPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onUIStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onUIDestory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            outState.putBundle(KEY_DATA, mBundle);
        }
        if(getPresenter() != null){
            getPresenter().onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getBundle(KEY_DATA) != null) {
                mBundle = savedInstanceState.getBundle(KEY_DATA);
            }
        }
        getPresenter().onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * getData:得到界面必需的Bundle数据. <br/>
     *
     * @return
     */
    public Bundle getData() {
        return mBundle;
    }

    protected String getRequestPage(){
        return this.getPageName();
    }
}
