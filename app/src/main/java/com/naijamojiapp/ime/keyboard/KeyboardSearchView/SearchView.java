/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naijamojiapp.ime.keyboard.KeyboardSearchView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.naijamojiapp.R;
import com.naijamojiapp.ime.keyboard.KeyboardSwitcher;
import com.naijamojiapp.ime.keyboard.KeyboardView;
import com.naijamojiapp.ime.latin.common.StringUtils;


public final class SearchView extends LinearLayout {
    public static AppCompatEditText mSearchView;
    public TouchActionListener touchActionListener;
    public static boolean shouldAddSpace;
    private ImageView mIvBack;

    public SearchView(Context context) {
        super(context);
    }

    @Override
    public int getVisibility() {
        return super.getVisibility();
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            if (touchActionListener != null) {
                mSearchView.requestFocus();
                mSearchView.setCursorVisible(true);
                touchActionListener.onSearchBarTouched();
            }
            return true;
        }
    };

    public SearchView(Context context, final AttributeSet attrs) {
        super(context, attrs);
         setFocusableInTouchMode(true);
    }

    public interface TouchActionListener {
        void onBackArrowClicked();
        void onSearchBarTouched();
    }

    public void setTouchListener(TouchActionListener touchActionListener2) {
        touchActionListener = touchActionListener2;
    }

    public static void showCursor() {
         mSearchView.requestFocus();
         mSearchView.setCursorVisible(true);
    }

    public Editable getText() {
        return mSearchView.getText();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
         mSearchView = (AppCompatEditText) findViewById(R.id.edt_search);
         mSearchView.setOnTouchListener(touchListener);
         mIvBack = (ImageView) findViewById(R.id.iv_back);
        // leftIconView = (TextView) findViewById(C5243R.C5244id.search_bar_left_icon_view);
        // mIvBack.setTypeface(Themes.getInstance().getCurrentKBFontTypeface());
        mIvBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (touchActionListener != null) {
                    touchActionListener.onBackArrowClicked();
                }
            }
        });
        mSearchView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                KeyboardSwitcher.Companion.getInstance().getWords(s);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
      /*  this.crossMarkView = (TextView) findViewById(C5243R.C5244id.search_bar_cross_view);
        this.crossMarkView.setTypeface(Themes.getInstance().getCurrentKBFontTypeface());
        this.crossMarkView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editText.setText("");
                crossMarkView.setVisibility(8);
                updateAutoSpaceFlag(false);
                if (touchActionListener != null) {
                    touchActionListener.onSearchBarTouched();
                }
            }
        });*/
    }
    public static void insertTextIntoSearchBar(String str) {
        String obj = mSearchView.getText().toString();
        String str2 = "";
        try {
            str2 = (String) obj.subSequence(mSearchView.getSelectionStart(), mSearchView.length());
        } catch (IndexOutOfBoundsException e) {}

        if (str2 == null || str2.length() <= 0) {
            mSearchView.getEditableText().append(str);
            Log.i("string" + "====>" , str);
        } else {
            Log.i("string" + "====>" , str);
            mSearchView.getEditableText().insert(mSearchView.getSelectionStart(), str);
        }
    }

    public void setCursorVisible(boolean cursorVisible) {
       mSearchView.setCursorVisible(cursorVisible);
    }

    public String getTextInEditor() {
        return mSearchView.getText().toString();
    }

    public CharSequence getTextBeforeCursor(int i) {
        if (mSearchView != null) {
            int selectionStart = mSearchView.getSelectionStart();
            String obj = mSearchView.getText() != null ? mSearchView.getText().toString() : "";
            if (obj != null && !obj.isEmpty() && selectionStart > 0 && selectionStart <= obj.length()) {
                return obj.subSequence(selectionStart - i, selectionStart);
            }
        }
        return "";
    }

    public static void updateAutoSpaceFlag(boolean mShouldAddSpace) {
        shouldAddSpace = mShouldAddSpace;
    }

    public boolean getAutoSpaceFlag() {
        return shouldAddSpace;
    }

    public boolean hasSpaceInEnd() {
        String textInEditor = getTextInEditor();
        if (!StringUtils.isEmpty(textInEditor) && textInEditor.charAt(textInEditor.length() - 1) == ' ') {
            return true;
        }
        return false;
    }

    public void handleDeleteKey() {
        Editable text = mSearchView.getText();
        if (text.length() > 0 && mSearchView.getSelectionStart() > 0) {
            mSearchView.getText().delete(mSearchView.getSelectionStart() - 1, mSearchView.getSelectionStart());
            mSearchView.setSelection(mSearchView.getSelectionStart());
        }
      /*  if (text.toString().isEmpty()) {
            this.crossMarkView.setVisibility(8);
        }*/
    }

    public void clearFocus() {
        super.clearFocus();
        mSearchView.clearFocus();
    }

}
