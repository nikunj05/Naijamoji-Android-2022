package com.naijamojiapp.app.customview

import android.app.ProgressDialog
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.text.Spanned
import android.text.SpannableString
import androidx.core.content.ContextCompat
import com.naijamojiapp.R


class CustomProgressDialog : ProgressDialog {

    var mContext: Context? = null


    constructor(context: Context, theme: Int) : super(context, theme) {
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val message = context.getString(R.string.pls_wait)
        val spannableString = SpannableString(message)

      /*  val typefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(context.assets, "fonts/NunitoSansLight.ttf"))
        spannableString.setSpan(typefaceSpan, 0, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
*/
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        isIndeterminate = true
        setCancelable(false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawable = ProgressBar(mContext).indeterminateDrawable.mutate()
            drawable.setColorFilter(ContextCompat.getColor(this.mContext!!, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN)
            setIndeterminateDrawable(drawable)
        }
        setCanceledOnTouchOutside(false)
        setMessage(spannableString)
        val textView = findViewById<TextView>(android.R.id.message)

    }

}
