package com.example.richeditview.edit;

import android.content.Context;
import android.content.Intent;

import com.example.richeditview.base.IBasePresenter;
import com.example.richeditview.base.IBaseView;

public interface EditContact {
    interface IEditView extends IBaseView {
        void showPicture(String imagePath);
    }

    interface IEditPresenter extends IBasePresenter {
        //P层的抽象方法设计主要就是这个业务逻辑
        void save();
    }

    interface IEditModel {
        void save(Context context, String key, String value);

        void getContent(GetContentListener getContentListener);

        interface GetContentListener {
            void onSuccess(String Content);
        }
    }
}
