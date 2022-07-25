package com.example.richeditview.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author csj
 */
public class SpUtil {
    private static SharedPreferences sp;

    public static void putString(Context context,String key,String value){
        if (sp==null){
            sp= context.getSharedPreferences("sp",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }
    public static String getString(Context context ,String key){
        if (sp==null){
            sp=context.getSharedPreferences("sp",Context.MODE_PRIVATE);
        }
        return sp.getString(key,"");
    }
}
