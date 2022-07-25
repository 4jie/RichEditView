package com.example.richeditview.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseActivity<P extends IBasePresenter> extends Activity implements IBaseView {
    public P mIBasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initView();
        initListener();
        mIBasePresenter = createPresenter();
        mIBasePresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIBasePresenter.detachView();
        mIBasePresenter = null;
    }

    /**
     * 获取Content区域的Id
     *
     * @return 返回Id
     */
    public abstract int getContentViewId();

    /**
     * 初始化控件，进行Id绑定
     */
    public abstract void initView();

    /**
     * 初始化监听器，设置点击事件
     */
    public abstract void initListener();

    /**
     * 创建Presenter
     */
    public abstract P createPresenter();

}
