package com.example.richeditview.edit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.richeditview.base.BasePresenter;
import com.example.richeditview.base.IBasePresenter;
import com.example.richeditview.base.IBaseView;

import java.nio.channels.SelectableChannel;

/**
 * @author csj
 */
public class EditPresenter extends BasePresenter<EditContact.IEditView> implements EditContact.IEditPresenter {
    private EditContact.IEditView mIEditView;
    public EditActivity mEditActivity=new EditActivity();

    public EditPresenter(EditContact.IEditView IEditView) {
        this.mIEditView = IEditView;
    }

    @Override
    public void save() {

    }
}
