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

class StickersView : RelativeLayout/*, SendStickersInterface*/ {
    private var rvStickerGrid: RecyclerView? = null
    private var cardlistAdapter: KbStickersAdapter? = null
    var mLinearLayoutManager: GridLayoutManager? = null
    var mEmojiList = ArrayList<EmojiListResponse.Result>()
    var simpleSeekBar: SeekBar? = null
    var ivPlus: ImageView? = null
    var ivMinus: ImageView? = null
    var mContext: Context? = null
    var rlOne: RelativeLayout? = null
    var rlTwo: RelativeLayout? = null
    var mStickersView: StickersView? = null
    var mDirectStickerSend: StickersView? = null
    var progressChangedValue = 0
    var mActions: Actions? = null

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
        rvStickerGrid = findViewById<View>(R.id.sticker_grid) as RecyclerView
        simpleSeekBar = findViewById<View>(R.id.simpleSeekBar) as SeekBar
        rlOne = findViewById<View>(R.id.rlOne) as RelativeLayout
        rlTwo = findViewById<View>(R.id.rlTwo) as RelativeLayout

        ivPlus = findViewById<View>(R.id.ivPlus) as ImageView
        ivMinus = findViewById<View>(R.id.ivMinus) as ImageView
        mLinearLayoutManager = GridLayoutManager(context, 4)
   //     callEmojiListWs()

   /*     ivPlus!!.setOnClickListener {

            if (progressChangedValue == 0) {
                simpleSeekBar!!.setProgress(50)
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                cardlistAdapter = KbStickersAdapter(context, mEmojiList, this@StickersView, 100)
                rvStickerGrid!!.adapter = cardlistAdapter
            } else if (progressChangedValue == 50) {
                simpleSeekBar!!.setProgress(100)
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                cardlistAdapter = KbStickersAdapter(context, mEmojiList, this@StickersView, 140)
                rvStickerGrid!!.adapter = cardlistAdapter
            }
        }

        ivMinus!!.setOnClickListener {
            if (progressChangedValue == 100) {
                simpleSeekBar!!.setProgress(50)
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                cardlistAdapter = KbStickersAdapter(context, mEmojiList, this@StickersView, 100)
                rvStickerGrid!!.adapter = cardlistAdapter
            } else if (progressChangedValue == 50) {
                simpleSeekBar!!.setProgress(0)
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                cardlistAdapter = KbStickersAdapter(context, mEmojiList, this@StickersView, 70)
                rvStickerGrid!!.adapter = cardlistAdapter
            }
        }

        simpleSeekBar!!.setEnabled(false)
        simpleSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                if (progressChangedValue <= 49) {
                    mLinearLayoutManager!!.setSpanCount(4)
                    mLinearLayoutManager!!.requestLayout()
                } else if (progressChangedValue >= 50 && progressChangedValue <= 99) {
                    mLinearLayoutManager!!.setSpanCount(3)
                    mLinearLayoutManager!!.requestLayout()
                } else if (progressChangedValue == 100) {
                    mLinearLayoutManager!!.setSpanCount(2)
                    mLinearLayoutManager!!.requestLayout()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/
    }

    fun callEmojiListWs() {
      //  ApiClass().getInstanse(context)!!.mEmojiList
    /*    if (this != null) {
            AndroidNetworking.post(Constants.INSTANCE.URLLOCAL + Constants.INSTANCE.strWS_emoji_listing)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(EmojiListResponse::class.java, object : ParsedRequestListener<EmojiListResponse> {
                        override fun onResponse(response: EmojiListResponse?) {
                            Log.i("response", response.toString())
                            var mSuccess = false
                            if (response != null)
                                if (response.status!!.success == (Constants.INSTANCE.one)) {
                                    mSuccess = true
                                }
                            if (mSuccess) {
                                mEmojiList = response!!.status!!.result!!
    rvStickerGrid!!.layoutManager = mLinearLayoutManager
        cardlistAdapter = KbStickersAdapter(context, mEmojiList, this@StickersView, 0)
        rvStickerGrid!!.adapter = cardlistAdapter
                                Log.i("error", "mSuccess")
                            } else {
                                try {
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        override fun onError(anError: ANError) {
                            Log.i("error", anError.toString())
                        }
                    })
        }*/

    }

  /*  override fun sendSticekrs(context: Context, mImageUrl: String,isSendOrNot:String, mStickerUrl:String, mStickerId:String) {
        if(isSendOrNot.equals("0")){
            Glide.with(context)
                    .asBitmap().load(mImageUrl)
//                    .asBitmap().load(R.raw.aaasticker)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(bitmap: Bitmap, o: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, b: Boolean): Boolean {
                            RichInputConnection.doCommitContent("Recovery animation", bitmapToFile(bitmap, context))
                            return false
                        }
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    }).submit()
        }else{
            val create = AlertDialog.Builder(context).create()
            val inflate = create.getLayoutInflater().inflate(R.layout.dialog_keyboard,null)
            val rlLight = inflate.findViewById<View>(R.id.rlLight) as RelativeLayout
            val rlMedium = inflate.findViewById<View>(R.id.rlMedium) as RelativeLayout
            val rlDark = inflate.findViewById<View>(R.id.rlDark) as RelativeLayout
            val rlGender = inflate.findViewById<View>(R.id.rlGender) as RelativeLayout
            val rlStudioMode = inflate.findViewById<View>(R.id.rlStudioMode) as RelativeLayout
            create.window.setGravity(Gravity.BOTTOM)
            if (create.getWindow() != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    create.window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
                } else {
                    create.window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                }
            }
            create.setView(inflate)

            rlLight.setOnClickListener {
                rlOne!!.visibility = View.GONE
                rlTwo!!.visibility = View.GONE
                create.cancel()
                if (Preferences.INSTANCE!!.loginStatus) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","0")
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, LoginAndRegistrationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","0")
                    context.startActivity(intent)
                }

            }
            rlMedium.setOnClickListener {
                rlOne!!.visibility = View.GONE
                rlTwo!!.visibility = View.GONE
                create.cancel()
                val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getWindowToken(), 0)
                if (Preferences.INSTANCE!!.loginStatus) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","1")
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, LoginAndRegistrationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","1")
                    context.startActivity(intent)
                }
            }
            rlDark.setOnClickListener {
                rlOne!!.visibility = View.GONE
                rlTwo!!.visibility = View.GONE
                create.cancel()
                val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getWindowToken(), 0)
                if (Preferences.INSTANCE!!.loginStatus) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","2")
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, LoginAndRegistrationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","2")
                    context.startActivity(intent)
                }
            }
            rlGender.setOnClickListener {
                rlOne!!.visibility = View.GONE
                rlTwo!!.visibility = View.GONE
                create.cancel()
                val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getWindowToken(), 0)
                if (Preferences.INSTANCE!!.loginStatus) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","3")
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, LoginAndRegistrationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","3")
                    context.startActivity(intent)
                }
            }
            rlStudioMode.setOnClickListener {
               // Log.i("adapter"+"==StickerView",mStickerUrl + " " + mStickerId)
                rlOne!!.visibility = View.GONE
                rlTwo!!.visibility = View.GONE
                create.cancel()
                val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(getWindowToken(), 0)
                if (Preferences.INSTANCE!!.loginStatus) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","4")
                    intent.putExtra("selected_sticker",mStickerUrl)
                    intent.putExtra("selected_id", mStickerId)
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, LoginAndRegistrationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("formKeyboard","4")
                    context.startActivity(intent)
                }
            }

            create.setOnCancelListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.cancel()
            }
            create.setOnDismissListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.dismiss()
            }
            rlOne!!.visibility = View.VISIBLE
            rlTwo!!.visibility = View.VISIBLE
            create.show()
        }
    }*/

    private fun bitmapToFile(bitmapp: Bitmap, context: Context): File {
        var file = File(context.getFilesDir(), ".")
        file = File(file, ".webp")
       /* if (!file.exists()) {
            file.mkdirs()
        }*/
        var bitmap : Bitmap ? = null
        if(RichInputConnection.getPackageName()!= null && RichInputConnection.getPackageName().equals("com.hike.chat.stickers")){
            bitmap = bitmapp
        }else{
            bitmap = makeTransparent(bitmapp)
        }
        var quality = 100
        var stream: FileOutputStream? = null
        try {
            stream = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true)
        var byteArrayLength = 100000
        var bos: ByteArrayOutputStream? = null
        while (byteArrayLength / 1000 >= 100) {
            bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP,
                    quality,
                    bos)
            byteArrayLength = bos.toByteArray().size
            quality -= 10
            Log.w("IMAGE SIZE IS NOW", byteArrayLength.toString() + "")
        }
        try {
            stream!!.write(bos!!.toByteArray())
            stream.flush()
            stream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


    fun makeTransparent(bit:Bitmap):Bitmap {
        val width = bit.getWidth()
        val height = bit.getHeight()
        val myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val allpixels = IntArray(myBitmap.getHeight() * myBitmap.getWidth())
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight())
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height)
        for (i in 0 until myBitmap.getHeight() * myBitmap.getWidth())
        {
            if (allpixels[i] == Color.WHITE)
                allpixels[i] = Color.alpha(Color.TRANSPARENT)
        }
        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight())
        return myBitmap
    }


}