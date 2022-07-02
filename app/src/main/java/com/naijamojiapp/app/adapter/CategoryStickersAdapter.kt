package com.naijamojiapp.app.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.naijamojiapp.R
import com.naijamojiapp.app.interfaces.StickerClicked
import com.naijamojiapp.app.response.EmojiListResponse
import java.util.ArrayList


class CategoryStickersAdapter(private val mcontext: Context, mEmojiList: ArrayList<EmojiListResponse.Result>, private val mStickerClicked: StickerClicked) : RecyclerView.Adapter<CategoryStickersAdapter.MyViewHolder>() {
    private var mEmojiList = ArrayList<EmojiListResponse.Result>()

    init {
        this.mEmojiList = mEmojiList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_sticker_iv, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = mEmojiList[position]

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
            mStickerClicked.mStickerClicked(response,"0")
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            //GoToStudioMode
            mStickerClicked.mStickerClicked(response, "1")
            true
        })
    }

    override fun getItemCount(): Int {
        return mEmojiList.size
    }


    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var iv_sticker: ImageView

        init {
            iv_sticker = itemView!!.findViewById(R.id.iv_sticker) as ImageView
        }
    }
}