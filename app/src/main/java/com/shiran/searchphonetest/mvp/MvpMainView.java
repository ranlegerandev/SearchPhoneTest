package com.shiran.searchphonetest.mvp;


/**
 * Created by Administrator on 2017/10/2.
 */

public interface MvpMainView extends MvpLoadingView{

    void showToast(String msg);
    void updateView();

}
