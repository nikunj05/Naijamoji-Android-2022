package com.naijamojiapp.ime.keyboard.KeyboardSearchView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

public class AppCompatEditText extends EditText {

    public AppCompatEditText(Context context) {
        super(context);
    }

    public AppCompatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCompatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);

    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);

    }

    /*public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return C1239i.m5622a(super.onCreateInputConnection(editorInfo), editorInfo, this);
    }*/
}
