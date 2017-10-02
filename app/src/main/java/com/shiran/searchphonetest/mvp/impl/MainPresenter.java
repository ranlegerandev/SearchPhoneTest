package com.shiran.searchphonetest.mvp.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.shiran.searchphonetest.business.HttpUtils;
import com.shiran.searchphonetest.model.Phone;
import com.shiran.searchphonetest.mvp.MvpMainView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/2.
 */

public class MainPresenter extends BasePresenter{
    private static final String TAG = "MainPresenter";

    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm"; //请求的服务器地址
    MvpMainView mvpMainView;
    Phone mPhone;
    public MainPresenter(MvpMainView mainView) {
        mvpMainView = mainView;
    }
    public Phone getPhoneInfo() {
        return mPhone;
    }
    public void sarchPhoneInfo(String phone) {
        if (phone.length()!= 11) {
            mvpMainView.showToast("请输入正确的手机号");
            return;
        }
        mvpMainView.showLoading();
        //http请求处理逻辑
        sendHttp(phone);
    }

    private void sendHttp(String phone) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("tel",phone);
        HttpUtils httpUtils = new HttpUtils(new HttpUtils.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                //截取json字符串
                String json  = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index,json.length());
                //使用JsonObject解析
                mPhone = parseModelWithOrgJson(json);
                //使用Gson解析
                mPhone = parseModelWithGson(json);
                //使用FastJson解析
                mPhone = parseModelWithFastJson(json);

                mvpMainView.hidenLoading();
                mvpMainView.updateView();

            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUtils.senGetHttp(mUrl, map);
    }

    private Phone parseModelWithOrgJson(String json) {
        Phone phone = new Phone();
        try {
            JSONObject obj = new JSONObject(json);
            String value = obj.getString("telString");
            phone.setTelString(value);
            Log.e(TAG, phone.getTelString().toString());

            value = obj.getString("province");
            phone.setProvince(value);
            Log.e(TAG, phone.getProvince().toString());

            value = obj.getString("catName");
            phone.setCatName(value);
            Log.e(TAG, phone.getCatName().toString());

            value = obj.getString("carrier");
            phone.setCarrier(value);
            Log.e(TAG, phone.getCarrier().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }

    private Phone parseModelWithGson(String json) {
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json, Phone.class);
        return phone;
    }

    private Phone parseModelWithFastJson(String json) {
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json, Phone.class);
        return phone;
    }

}
