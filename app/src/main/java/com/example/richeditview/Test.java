package com.example.richeditview;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Test {


    boolean function(View view1){

        if(view1 instanceof EditText){
//            EditText editText2=(EditText) view1;
            return true;
        }else{
//            ImageView imageView2=(ImageView) view1;
            return false;
        }


    }


}
