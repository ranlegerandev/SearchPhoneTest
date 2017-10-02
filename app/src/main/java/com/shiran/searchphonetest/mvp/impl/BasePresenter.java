package com.shiran.searchphonetest.mvp.impl;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/2.
 */

public class BasePresenter {

    Context mContext;
    public void attach(Context context){
        mContext = context;
    }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){
        mContext = null;
    }

}
