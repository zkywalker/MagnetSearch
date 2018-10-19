package org.zky.tool.magnetsearch.base;

public interface IUI extends IUIState {

    void showLoadingView();

    void dismissLoadingView();

    String getPageName();

    void showToast(String content);

    void showToast(int stringId);

    void showSnack(int stringId);

    void showSnack(String content);

}
