package com.naijamojiapp.app.studiomode;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.naijamojiapp.R;
import com.naijamojiapp.app.utils.Preferences;

import java.util.Random;

public class TextEditorDialogFragment extends DialogFragment {

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_TYPEFACE = "extra_typeface";
    public EditText mAddTextEditText;
    public TextView tvCount;
    private TextView mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;
    private int mColorCode;
    private TextEditor mTextEditor;
    private ImageView ivSelectText;
    Typeface typeFace;
    private String mFontType = "";
    private String currentTextValues = "";
    private String currentTextFieldValues = "";

    public interface TextEditor {
        void onDone(Spannable inputText, int colorCode, String typeface);

    }

    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode,
                                                @NonNull String typeface) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        args.putString(EXTRA_TYPEFACE, typeface);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "", ContextCompat.getColor(appCompatActivity, R.color.white), null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setDimAmount(0.0f);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        tvCount = view.findViewById(R.id.tvCount);
        mAddTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int index, int i1, int newCharLength) {
                String newTextValues = index+"."+i1+"."+newCharLength;
                if (!newTextValues.equals(currentTextValues) && !currentTextFieldValues.equals(charSequence.toString()) ) {
                    currentTextValues = newTextValues;
                    currentTextFieldValues = charSequence.toString();
                    mAddTextEditText.setText(fontcolor(charSequence.toString()), TextView.BufferType.SPANNABLE);
                    mAddTextEditText.setSelection(charSequence.toString().length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);
        ivSelectText = view.findViewById(R.id.iv_select_text);
        ivSelectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectTextStyleDialog();
            }
        });


        if (getArguments().getString(EXTRA_TYPEFACE) != null && !TextUtils.isEmpty(getArguments().getString(EXTRA_TYPEFACE))) {
            Typeface type2 = Typeface.createFromAsset(getActivity().getAssets(), getArguments().getString(EXTRA_TYPEFACE));
            mFontType = getArguments().getString(EXTRA_TYPEFACE);
            mAddTextEditText.setTypeface(type2);
        } else {
            Typeface type2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/1.ttf");
            mFontType = "fonts/1.ttf";
            mAddTextEditText.setTypeface(type2);
        }
        //  mAddTextEditText.setTextColor(mColorCode);

        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                Spannable inputText = fontcolor(mAddTextEditText.getText().toString());
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    mTextEditor.onDone(inputText, mColorCode, mFontType);
                }
            }
        });

    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    private Spannable fontcolor(String text) {

        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        Spannable word = new SpannableString(text);
        if(Preferences.Companion.getINSTANCE().getGetFontMode().equals("0")){
            try {
                for (int i = 0; i < word.length(); i++) {
                    word.setSpan(new ForegroundColorSpan(androidColors[new Random().nextInt(androidColors.length)]), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("colorCodes" + "===>", word.toString() + "===========" + i + "========" + new Gson().toJson(androidColors[new Random().nextInt(androidColors.length)]));
                }
                return word;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return word;
    }


  /*  private Spannable buildRainbowText(String pack_name) {
        int[] colors = new int[]{Color.RED, 0xFFFF9933, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED, 0xFFFF9933, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED, 0xFFFF9933, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED, 0xFFFF9933, Color.YELLOW, Color.GREEN, Color.BLUE};
        Spannable word = new SpannableString(pack_name);
        for (int i = 0; i < word.length(); i++) {
            word.setSpan(new ForegroundColorSpan(colors[i]), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return word;
    }*/

    private void mSelectTextStyleDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_select_text_style);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tvFontOne = dialog.findViewById(R.id.tv_font_one);
        Typeface type1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/1.ttf");
        tvFontOne.setTypeface(type1);

        TextView tvFontTwo = dialog.findViewById(R.id.tv_font_two);
        Typeface type2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/2.ttf");
        tvFontTwo.setTypeface(type2);

        TextView tvFontThree = dialog.findViewById(R.id.tv_font_three);
        Typeface type3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/3.ttf");
        tvFontThree.setTypeface(type3);

        TextView tvFontFour = dialog.findViewById(R.id.tv_font_four);
        Typeface type4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/4.ttf");
        tvFontFour.setTypeface(type4);

        tvFontOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFont("1");
                dialog.dismiss();
            }
        });

        tvFontTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFont("2");
                dialog.dismiss();
            }
        });

        tvFontThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFont("3");
                dialog.dismiss();
            }
        });

        tvFontFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFont("4");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setFont(String position) {
        if (position != null && !TextUtils.isEmpty(position) && position.equalsIgnoreCase("1")) {
            setFontTypeFace("fonts/1.ttf");
        } else if (position != null && !TextUtils.isEmpty(position) && position.equalsIgnoreCase("2")) {
            setFontTypeFace("fonts/2.ttf");
        } else if (position != null && !TextUtils.isEmpty(position) && position.equalsIgnoreCase("3")) {
            setFontTypeFace("fonts/3.ttf");
        } else if (position != null && !TextUtils.isEmpty(position) && position.equalsIgnoreCase("4")) {
            setFontTypeFace("fonts/4.ttf");
        } else {
            setFontTypeFace("fonts/1.ttf");
        }

    }

    private void setFontTypeFace(String type) {
        mFontType = type;
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), type);
        mAddTextEditText.setTypeface(typeFace);
        //mAddTextEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    }

    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }

    public EditText AddTextEditText() {
        return mAddTextEditText;
    }
}
