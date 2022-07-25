package com.example.richeditview.base;

public class BasePresenter<V extends IBaseView> implements IBasePresenter {
    public IBaseView mView;

    @Override
    public void attachView(IBaseView view) {
        this.mView=view;
    }

    @Override
    public void detachView() {
        if (mView!=null){
            mView=null;
        }
    }
}
