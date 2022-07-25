package com.example.richeditview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author csj
 */
public class RichEditText extends androidx.appcompat.widget.AppCompatEditText {
    private Context mContext;
    private List<String> mContentList;

    public static final String M_BITMAP_TAG = "☆";
    private static final String M_NEW_LINE_TAG = "\n";

    public RichEditText(Context context) {
        this(context,null);
//        super(context);
//        init(context);

    }

    public RichEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,-1);
//        super(context, attrs);
//        init(context);
    }

    public RichEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
//        setSelection(0);
    }

    private void init(Context context) {
        mContext = context;
        mContentList = getmContentList();
        insertData();
    }

    private void insertData() {
        if (!mContentList.isEmpty()) {
            for (String str : mContentList) {
                if (str.contains(M_BITMAP_TAG)) {
                    //判断是否是图片地址,其实就是判断字符串里面是否含有这个字符
                    //还原地址字符串
                    String path = str.replace(M_BITMAP_TAG, "");
                    //用指定的文字替换序列替换此字符串中与文字目标序列匹配的每个子字符串。
                    // 替换从字符串的开头到结尾进行，
                    // 例如，将字符串“aaa”中的“aa”替换为“b”将导致“ba”而不是“ab”。
                    //参数：
                    //target - 要替换的 char 值的序列
                    //replacement - char 值的替换序列
                    Bitmap bitmap = getSmallBitmap(path, 480, 800);
                    //插入图片
                    insertBitmap(path, bitmap);
                } else {
                    //插入文字
                    SpannableString spannableString = new SpannableString(str);
                    append(spannableString);
                }
            }
        }
    }

    private SpannableString insertBitmap(String path, Bitmap bitmap) {
        Editable editable = getEditableText();
        //获取光标所在位置
        int index = getSelectionStart();
        //插入换行符,使图片单独占一行
        SpannableString newLine = new SpannableString("\n");
        editable.insert(index, newLine);
        path = M_BITMAP_TAG + path + M_BITMAP_TAG;
        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString(path);
        //根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        //用ImageSpan对象替换指定的字符串
        spannableString.setSpan(imageSpan, 0, path.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //将选择的图片追加到EditText中光标的位置
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        //插入后换行
        editable.insert(index, newLine);
        return spannableString;
    }

    public void insertBitmap(String path) {
        Bitmap bitmap = getSmallBitmap(path, 480, 800);
        insertBitmap(path, bitmap);
    }

    public Bitmap getSmallBitmap(String path, int width, int height) {
        //第一次采样获取图片的压缩比例
        BitmapFactory.Options options = new BitmapFactory.Options();
        //默认值为false设置为true表示在解码的时候不会返回bitmap，本地图片解码之后bitmap为null，只计算图片的宽高，然后根据得到的图片获取来放大或缩小图片的分辨率
        //分辨率决定了位图的图像细节的精细程度，所包含的像素越多，图像就越清晰，同时也会增加图片的占用空间
        options.inJustDecodeBounds = true;
        //解码一张本地图片，但是解码之后图片太大，会导致app性能下降甚至是OOM
        //这里第一次加载图片，这是只会加载图片的边框，不会加载图片具体的像素点
        BitmapFactory.decodeFile(path, options);
        //为防止图片太大，所以我们先计算图片的分辨率
        //inSampleSize：修改像素密度只能修改图像的分辨率，但不能改变bitmap的大小，而inSampleSize不仅可以修改分辨率，还可以修改图片的大小
        //假如默认情况（inSampleSize = 1）下，图像的分辨率是205x205，占用内存0.16031265M，那么如果将inSampleSize 设置成2，那么分辨率的宽和高分别除以2，
        // 占用的内存也会变小，最终图像的分辨率是103x103，占用内存为0.040470123M，bitmap大小整整缩小了4倍

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        //描述有关显示的一般信息的结构，例如其大小、密度和字体缩放
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        //displayMetrics.widthPixels:以像素为单位的可显示大小的绝对宽度
        int screenWidth = displayMetrics.widthPixels;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int wHeight = screenWidth * bitmapHeight / bitmapWidth;
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, wHeight, true);
        // 参数：
        //src – 源位图。
        //dstWidth – 新位图的所需宽度。
        //dstHeight – 新位图的所需高度。
        //filter – 缩放位图时是否应使用双线性过滤。如果这是真的，那么在缩放时将使用双线性滤波，
        // 这会以更差的性能为代价获得更好的图像质量。如果这是错误的，则使用最近邻缩放代替，
        // 这将具有更差的图像质量但更快。推荐的默认设置是将过滤器设置为“真”，因为双线性过滤的成本通常是最小的
        // 并且改进的图像质量很重要。
        return bitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取原图的宽高
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        //定义缩放比例
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            //Math.round函数：参数是浮点数，是四舍六入取整函数
            final int heightRatio = Math.round((float) outHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) outWidth / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    /**
     * 用集合的形式获取控件里的内容
     */
    public List<String> getmContentList() {
        if (mContentList == null) {
            mContentList = new ArrayList<>();
        }
        String content = Objects.requireNonNull(getText()).toString().replace(M_NEW_LINE_TAG, "");
        if (content.length() > 0 && content.contains(M_BITMAP_TAG)) {
            String[] split = content.split("☆");
            mContentList.clear();
            mContentList.addAll(Arrays.asList(split));
        } else {
            mContentList.add(content);
        }
        return mContentList;
    }

    float oldY = 0;
    int maxDistance = 20;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                requestFocus();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                if (Math.abs(oldY - newY) > maxDistance) {
                    clearFocus();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
