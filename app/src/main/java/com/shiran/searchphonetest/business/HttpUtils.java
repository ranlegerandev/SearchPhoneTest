package com.shiran.searchphonetest.business;

import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Mvp模式网络请求
 * Created by Liujunlong on 2017/10/2.
 */

public class HttpUtils {

    String mUrl;
    Map<String,String> mParam;
    HttpResponse mHttpResponse;
    android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper());

    private OkHttpClient client = new OkHttpClient();
    public interface HttpResponse {
        void onSuccess(Object object);
        void onFail(String error);
    }
    public HttpUtils(HttpResponse response) {
        mHttpResponse = response;

    }

    public void sendPostHttp(String url, Map<String,String> param) {
        sendHttp(url,param,true);

    }

    public void senGetHttp(String url, Map<String,String> param) {
        sendHttp(url,param,false);

    }

    private void sendHttp(String Url, Map<String,String> param,boolean isPost) {
        mUrl = Url;
        mParam = param;
        //编写okhttp请求逻辑
        run(isPost);
    }
    private void run(boolean isPost) {
        //创建Request请求
        final Request request = createRequest(isPost);
        //创建请求队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mHttpResponse == null)
                    return;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()) {
                            mHttpResponse.onFail("请求失败：code");
                        } else {
                            try {
                                mHttpResponse.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                mHttpResponse.onFail("结果转换失败");
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpResponse != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }
        });
    }

    private Request createRequest(boolean isPost) {
        Request request;
        if (isPost) {
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            //遍历Mvp请求参数
            Iterator<Map.Entry<String,String>> iterator = mParam.entrySet().iterator();
            while ((iterator.hasNext())) {
                Map.Entry<String,String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request = new Request.Builder().url(mUrl)
                    .post(requestBodyBuilder.build())
                    .build();
        } else {
            String urlStr = mUrl+"?"+MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }

    private String MapParamToString(Map<String,String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str = stringBuilder.toString().substring(0,stringBuilder.length());
        return str;
    }
}
