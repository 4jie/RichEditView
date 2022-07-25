package com.example.richeditview.edit;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.richeditview.R;
import com.example.richeditview.util.SpUtil;
import com.example.richeditview.widget.RichEditText;

import java.util.Objects;

/**
 * @author csj
 */
public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private EditContact.IEditPresenter editPresenter;
    private Button mInsertPicture;
    private RichEditText mRichEditText;
    private final int CHOOSE_PHOTO = 2;
    private String content;
    private static final String key1 = "content";
    /**
     * 从sp中拿到的数据
     */
    private String getContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        getContent = SpUtil.getString(this, key1);
        Log.e(TAG, "onCreate: " + getContent);
        if (getContent != null) {
            String[] item = getContent.split("☆");
            for (String s : item) {
                Log.e(TAG, "onCreate: " + s);
                if (s.contains(".jpg")) {
                    Log.e(TAG, "图片：" + s);
                    mRichEditText.insertBitmap(s);
                } else {
                    Log.e(TAG, "文本：" + s);
                    Editable editable=mRichEditText.getEditableText();
                    editable.append(s);
                }
            }
        }
    }

    /**
     * 完全不可见的时候调用
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " + content);
        content = Objects.requireNonNull(mRichEditText.getText()).toString();
        //保存数据
        SpUtil.putString(this, key1, content);
    }

    public void initView() {
        mInsertPicture = findViewById(R.id.btn_insert_picture);
        mRichEditText = findViewById(R.id.rich_edit_view);
    }

    private void initData() {
        Log.e(TAG, "initData: ");
    }

    public void initListener() {
        mInsertPicture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum();
            }
        });
    }

    public void openAlbum() {
//        Intent intent = new Intent("android:intent.action.GET_CONTENT");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 在用户点击权限选择框之后会回调这个方法
     *
     * @param requestCode  请求码，是在ActivityCompat.requestPermission的时候设置的
     * @param permissions  包含所有请求权限的附加项的密钥,这个参数一般不会用到
     * @param grantResults 用户进行权限选择之后的结果一般封装在这个数组里
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                handleImage(data);
                Log.e(TAG, "onActivityResult: data="+data );
            }
        }
    }

    public void handleImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，就按照document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            //uri.getAuthority():获取uri的权限
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        Log.e(TAG, "handleImage: " + imagePath);
        showPicture(imagePath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = this.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void showPicture(String imagePath) {
        if (imagePath != null) {
            mRichEditText.insertBitmap(imagePath);
        }
    }
}