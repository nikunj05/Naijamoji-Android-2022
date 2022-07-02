package com.naijamojiapp.app.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*
import com.naijamojiapp.R
import com.naijamojiapp.app.customview.CustomDialog
import com.naijamojiapp.app.interfaces.RemoveStickerInterface
import com.naijamojiapp.app.interfaces.newApp.CustomStickerClicked
import com.naijamojiapp.app.response.newResponse.YourCustomStickersListResponse
import com.naijamojiapp.app.utils.CheckConnection
import java.lang.Exception

class YourStickersAdapter(private val mcontext: Context, mYourStickerList: ArrayList<YourCustomStickersListResponse.Result>,
                          private val mStickerClicked : CustomStickerClicked,
                          private val mStickerlongPressRemove : RemoveStickerInterface) : RecyclerView.Adapter<YourStickersAdapter.MyViewHolder>() {
    private var mYourStickerList = ArrayList<YourCustomStickersListResponse.Result>()

    init {
        this.mYourStickerList = mYourStickerList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_remove_sticker_iv, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = mYourStickerList[position]
        if (response.image != null && !TextUtils.isEmpty(response.image)) {
            Glide.with((mcontext).applicationContext).load(response.image).
                    apply(RequestOptions().placeholder(R.drawable.photos).
                            error(R.drawable.photos)).into(holder.iv_sticker)
        } else {
            Glide.with((mcontext).applicationContext).load(response.image).
                    apply(RequestOptions().placeholder(R.drawable.photos).
                    error(R.drawable.photos)).into(holder.iv_sticker)
        }

        holder.itemView.setOnClickListener {
            //Share
            try {
                mStickerClicked.mStickerClicked(response,"0")
            }catch (e:Exception){}
        }

        holder.ivRemoveSticker.setOnClickListener {
                val builder = AlertDialog.Builder(mcontext)
                builder.setMessage("Are you sure you want to remove this sticker?")
                builder.setPositiveButton("YES") { dialog, which ->
                    if (CheckConnection.getInstance(mcontext).isConnectingToInternet()) {
                        mStickerlongPressRemove.mStickerlongPress(response,position)
                    } else {
                        CustomDialog.instance!!.showalert(mcontext, mcontext.resources.getString(R.string.check_internet_connection))
                    }
                }
                builder.setNegativeButton("NO") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
        }
    }


    override fun getItemCount(): Int {
        return mYourStickerList.size
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var iv_sticker: ImageView
        var ivRemoveSticker: ImageView
        init {
            iv_sticker = itemView!!.findViewById(R.id.iv_sticker) as ImageView
            ivRemoveSticker = itemView.findViewById(R.id.ivRemoveSticker) as ImageView
        }
    }
}