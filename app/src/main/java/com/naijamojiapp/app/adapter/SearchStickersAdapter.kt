package com.naijamojiapp.app.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naijamojiapp.R
import com.naijamojiapp.app.interfaces.StickerClicked
import com.naijamojiapp.app.roomDB.entity.AllStickerList
import java.util.ArrayList


class SearchStickersAdapter(private val mcontext: Context, mEmojiList: ArrayList<AllStickerList>, private val mStickerClicked : StickerClicked) : RecyclerView.Adapter<SearchStickersAdapter.MyViewHolder>() {
    private var mEmojiList = ArrayList<AllStickerList>()

    init {
        this.mEmojiList = mEmojiList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_sticker_iv, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response = mEmojiList[position]

        if(response.image!= null && !TextUtils.isEmpty(response.image)){
            Glide.with((mcontext).applicationContext).
                    load(response.image).
                    apply(RequestOptions().placeholder(R.drawable.photos).error(R.drawable.photos)).
                    into(holder.ivSticker)
        }else{
            Glide.with((mcontext).applicationContext).
                    load(response.image).
                    apply(RequestOptions().placeholder(R.drawable.photos).error(R.drawable.photos)).
                    into(holder.ivSticker)
        }

        holder.itemView.setOnClickListener {
         //   mStickerClicked.mStickerClicked(response,"0")
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            //GoToStudioMode
         //   mStickerClicked.mStickerClicked(response, "1")
            true
        })
    }

    override fun getItemCount(): Int {
        return mEmojiList.size
    }


    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var ivSticker : ImageView
        init {
            ivSticker = itemView!!.findViewById(R.id.iv_sticker) as ImageView
        }

    }

}