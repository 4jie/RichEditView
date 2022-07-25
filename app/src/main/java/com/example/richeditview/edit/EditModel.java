package com.example.richeditview.edit;

import android.content.Context;

import com.example.richeditview.util.SpUtil;

public class EditModel implements EditContact.IEditModel {
    private Context mContext;

    @Override
    public void save(Context context, String key, String value) {
        SpUtil.putString(context, key, value);
    }

    @Override
    public void getContent(GetContentListener getContentListener) {
//        getContentListener.onSuccess();
    }
    private void getData(){
//        SpUtil.getString()
    }

}
