package com.shiran.searchphonetest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shiran.searchphonetest.model.Phone;
import com.shiran.searchphonetest.mvp.MvpMainView;
import com.shiran.searchphonetest.mvp.impl.MainPresenter;

public class MainActivity extends AppCompatActivity implements MvpMainView {
    private EditText mEtQueryPhone = null;
    private TextView mTextQuertResult = null;
    private TextView mTextQueryProvince = null;
    private TextView mTextQueryType = null;
    private TextView mTextQueryCarrier = null;
    private Button mBtnGoSearch = null;
    MainPresenter mMainPresenter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtQueryPhone = (EditText) findViewById(R.id.et_query_phone);
        mTextQuertResult = (TextView) findViewById(R.id.text_query_result);
        mTextQueryProvince = (TextView) findViewById(R.id.text_quert_province);
        mTextQueryType = (TextView) findViewById(R.id.text_quert_type);
        mTextQueryCarrier = (TextView) findViewById(R.id.text_quert_carrier);
        mBtnGoSearch= (Button) findViewById(R.id.btn_go_search);
        //实例化MainPresenter
        mMainPresenter = new MainPresenter(this);
        mMainPresenter.attach(this);
        mBtnGoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPresenter.sarchPhoneInfo(mEtQueryPhone.getText().toString());
            }
        });
    }

    /**
     * MvpMainView接口的方法
     */
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        Phone phone = mMainPresenter.getPhoneInfo();
        mTextQuertResult.setText("手机号码：" + phone.getTelString());
        mTextQueryProvince.setText("省份：" + phone.getProvince());
        mTextQueryType.setText("运营商：" + phone.getCatName());
        mTextQueryCarrier.setText("归属地运营商：" + phone.getCarrier());
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", "正在加载...", true, false);
        } else if (mProgressDialog.isShowing()) {
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("正在加载...");
        }
        mProgressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
