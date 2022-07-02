package com.naijamojiapp.app.adapter.newadapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.naijamojiapp.app.roomDB.entity.AllStickerList
import com.naijamojiapp.app.utils.Preferences
import com.naijamojiapp.R
import com.naijamojiapp.app.interfaces.newApp.SendDaynamicStickersAppInterface
import com.naijamojiapp.app.interfaces.newApp.SendLocalStickersAppInterface
import com.naijamojiapp.app.response.newResponse.StickerListByTagOrCategory
import com.naijamojiapp.ime.keyboard.KeyboardStickerView.SendDaynamicStickersInterface
import com.naijamojiapp.ime.keyboard.KeyboardStickerView.SendStickersInterface
import kotlin.collections.ArrayList

class LocalAndDaynamicStickersAdapter(private val mcontext: Context,
                                      val mEmojiList: ArrayList<AllStickerList>?,
                                      val mDaynamicEmojilist: ArrayList<StickerListByTagOrCategory.Result>?,
                                      val sendStickersInterface: SendLocalStickersAppInterface,
                                      val mSendDaynamicStickersInterface: SendDaynamicStickersAppInterface
                                      ) : RecyclerView.Adapter<LocalAndDaynamicStickersAdapter.MyViewHolder>() {
    var image = null
    var mHolder: MyViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_sticker_iv, null, true)
        mHolder = MyViewHolder(v)
        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(mEmojiList != null){
            val response = mEmojiList.get(position)
            if(response.is_studiomode.equals("1")){
                holder.rlStudioModeOrNot.visibility = View.VISIBLE
            }else{
                holder.rlStudioModeOrNot.visibility = View.GONE
            }

            if(!TextUtils.isEmpty(response.genderSticker)){
                if(response.is_gender_available.equals("0")){
                    loadImage(response.image)
                }else{
                    if(response.genderSticker.equals("MD")){
                        loadImage(response.img_m_dark)
                        response.genderSticker = "MD"
                    }else if(response.genderSticker.equals("MM")){
                        loadImage(response.img_m_medium)
                        response.genderSticker = "MM"
                    }else if(response.genderSticker.equals("ML")){
                        loadImage(response.img_m_light)
                        response.genderSticker = "ML"
                    }else if(response.genderSticker.equals("FD")){
                        loadImage(response.img_f_dark)
                        response.genderSticker = "FD"
                    }else if(response.genderSticker.equals("FM")){
                        loadImage(response.img_f_medium)
                        response.genderSticker = "FM"
                    }else if(response.genderSticker.equals("FL")){
                        loadImage(response.img_f_light)
                        response.genderSticker = "FL"
                    }

                }
            }else{
                if(response.is_gender_available.equals("0")){
                    loadImage(response.image)
                }else{
                    if(Preferences.INSTANCE!!.getGender.equals("M")){
                        //Male
                        if(Preferences.INSTANCE!!.getSkinTone.equals("D")){
                            loadImage(response.img_m_dark)
                            response.genderSticker = "MD"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("M")){
                            loadImage(response.img_m_medium)
                            response.genderSticker = "MM"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("L")){
                            loadImage(response.img_m_light)
                            response.genderSticker = "ML"
                        }
                    }else{
                        //Female
                        if(Preferences.INSTANCE!!.getSkinTone.equals("D")){
                            loadImage(response.img_f_dark)
                            response.genderSticker = "FD"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("M")){
                            loadImage(response.img_f_medium)
                            response.genderSticker = "FM"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("L")){
                            loadImage(response.img_f_light)
                            response.genderSticker = "FL"
                        }
                    }
                }
            }

            holder.ivSticker.setOnClickListener {
                try {
                    val drawable = holder.ivSticker.getDrawable() as BitmapDrawable
                    val bitmap = drawable.bitmap
                    sendStickersInterface.sendSticekrs(mcontext,position, response,"0",bitmap,holder.ivSticker)
                }catch (e:Exception){}
            }

            holder.ivSticker.setOnLongClickListener{
                try {
                    val drawable = holder.ivSticker.getDrawable() as BitmapDrawable
                    val bitmap = drawable.bitmap
                    sendStickersInterface.sendSticekrs(mcontext,position, response,"1",bitmap,holder.ivSticker)
                }catch (e:Exception){}
                true
            }
        }else{
            val response = mDaynamicEmojilist?.get(position)
            if(response!!.is_studiomode.equals("1")){
                holder.rlStudioModeOrNot.visibility = View.VISIBLE
            }else{
                holder.rlStudioModeOrNot.visibility = View.GONE
            }
            if(!TextUtils.isEmpty(response!!.genderSticker)){
                if(response.is_gender_available.equals("0")){
                    loadImage(response.image!!)
                }else{
                    if(response.genderSticker.equals("MD")){
                        loadImage(response.img_m_dark!!)
                        response.genderSticker = "MD"
                    }else if(response.genderSticker.equals("MM")){
                        loadImage(response.img_m_medium!!)
                        response.genderSticker = "MM"
                    }else if(response.genderSticker.equals("ML")){
                        loadImage(response.img_m_light!!)
                        response.genderSticker = "ML"
                    }else if(response.genderSticker.equals("FD")){
                        loadImage(response.img_f_dark!!)
                        response.genderSticker = "FD"
                    }else if(response.genderSticker.equals("FM")){
                        loadImage(response.img_f_medium!!)
                        response.genderSticker = "FM"
                    }else if(response.genderSticker.equals("FL")){
                        loadImage(response.img_f_light!!)
                        response.genderSticker = "FL"
                    }
                }
            }else{
                if(response.is_gender_available.equals("0")){
                    loadImage(response.image!!)
                }else{
                    if(Preferences.INSTANCE!!.getGender.equals("M")){
                        //Male
                        if(Preferences.INSTANCE!!.getSkinTone.equals("D")){
                            loadImage(response.img_m_dark!!)
                            response.genderSticker = "MD"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("M")){
                            loadImage(response.img_m_medium!!)
                            response.genderSticker = "MM"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("L")){
                            loadImage(response.img_m_light!!)
                            response.genderSticker = "ML"
                        }
                    }else{
                        //Female
                        if(Preferences.INSTANCE!!.getSkinTone.equals("D")){
                            loadImage(response.img_f_dark!!)
                            response.genderSticker = "FD"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("M")){
                            loadImage(response.img_f_medium!!)
                            response.genderSticker = "FM"
                        }else if(Preferences.INSTANCE!!.getSkinTone.equals("L")){
                            loadImage(response.img_f_light!!)
                            response.genderSticker = "FL"
                        }
                    }
                }
            }

            holder.ivSticker.setOnClickListener {
                try {
                    val drawable = holder.ivSticker.getDrawable() as BitmapDrawable
                    val bitmap = drawable.bitmap
                    mSendDaynamicStickersInterface.sendDaynamicSticekrs(mcontext,position, response,"0",bitmap,holder.ivSticker)
                }catch (e:Exception){}
            }

            holder.ivSticker.setOnLongClickListener{
                try {
                    val drawable = holder.ivSticker.getDrawable() as BitmapDrawable
                    val bitmap = drawable.bitmap
                    mSendDaynamicStickersInterface.sendDaynamicSticekrs(mcontext,position, response,"1",bitmap,holder.ivSticker)
                }catch (e:Exception){}
                true
            }
        }

    }

    fun loadImage(mUrl : String) {
        Glide.with(mcontext)
                .asBitmap().load(mUrl)
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(bitmap: Bitmap, o: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, b: Boolean): Boolean {
                     //   mBitmap = bitmap
                        return false
                    }
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(mHolder!!.ivSticker)
    }



    override fun getItemCount(): Int {
        if(mEmojiList != null){
            return mEmojiList.size
        }else if(mDaynamicEmojilist != null){
            return mDaynamicEmojilist.size
        }else{
            return 0
        }
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var ivSticker: ImageView
        var rlStudioModeOrNot : RelativeLayout
        var frameLayout: FrameLayout

        init {
            ivSticker = itemView!!.findViewById(R.id.iv_sticker) as ImageView
            rlStudioModeOrNot = itemView!!.findViewById(R.id.rlStudioModeOrNot) as RelativeLayout
            frameLayout = itemView.findViewById(R.id.frameLayout) as FrameLayout
        }
    }

}
