package com.naijamojiapp.ime.keyboard.KeyboardSearchView;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

public class MyInputConnection extends BaseInputConnection {

    private SpannableStringBuilder mEditable;

    MyInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
        SearchView customView = (SearchView) targetView;

    }

    @Override
    public Editable getEditable() {
        return mEditable;
    }

    // just adding this to show that text is being committed.
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        boolean returnValue = super.commitText(text, newCursorPosition);
        Log.i("TAG", "text: " + mEditable);
        return returnValue;
    }
}
