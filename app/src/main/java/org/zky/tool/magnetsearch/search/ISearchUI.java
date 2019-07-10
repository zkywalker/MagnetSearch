package org.zky.tool.magnetsearch.search;

import androidx.annotation.StringRes;

import org.zky.tool.magnetsearch.base.IUI;

import java.util.List;

public interface ISearchUI extends IUI {

    void snack(String s);

    void snack(@StringRes int s);

    void loadSearchData(List<SearchEntity> list);

    void ClearSearchData();

    void clickMenuIcon();

    void setListVisibility(boolean v);
}
