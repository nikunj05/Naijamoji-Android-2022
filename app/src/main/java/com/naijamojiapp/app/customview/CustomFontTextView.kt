package com.naijamojiapp.app.customview

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.widget.TextView
import android.graphics.Typeface
import com.naijamojiapp.R


class CustomFontTextView : TextView {
    private var typefaceCode: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            // Get Custom Attribute Name and value
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.FontTextView)
            typefaceCode = styledAttrs.getInt(R.styleable.FontTextView_fontType, -1)
            val typeface = TypefaceCache.get(context.assets, typefaceCode)
            setTypeface(typeface)

            styledAttrs.recycle()
            if (isInEditMode) {
                return
            }
        }
    }
    constructor(context: Context) : super(context) {}
    override fun setText(text: CharSequence?, type: TextView.BufferType) {
        var text = text
        if (text != null) {
            text = Html.fromHtml(text.toString())
        }
        super.setText(text, type)
    }
}