package com.naijamojiapp.ime.keyboard.KeyboardStickerView

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.naijamojiapp.R
import com.naijamojiapp.app.activity.HomeActivity
import com.naijamojiapp.app.activity.LoginAndRegistrationActivity
import com.naijamojiapp.app.response.EmojiListResponse
import com.naijamojiapp.app.utils.Constants
import com.naijamojiapp.app.utils.Preferences
import com.naijamojiapp.ime.keyboard.internal.KeyboardState
import com.naijamojiapp.ime.latin.RichInputConnection
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class RelativelayoutForTagView : LinearLayout{


    interface Actions {
        fun setCloseKeyboard()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int = R.attr.suggestionStripViewStyle) : super(context, attrs, defStyle) {
        val keyboardAttr = context.obtainStyledAttributes(attrs, R.styleable.Keyboard, defStyle, R.style.SuggestionStripView)
        keyboardAttr.recycle()
    }



    override fun onFinishInflate() {
        super.onFinishInflate()

    }

}