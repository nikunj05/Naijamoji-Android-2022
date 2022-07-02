package com.naijamojiapp.app.adapter.newadapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naijamojiapp.R
import com.naijamojiapp.app.response.SearchKeyboardTagListResponse
import com.naijamojiapp.app.roomDB.entity.TagList
import com.naijamojiapp.ime.keyboard.KeyboardStickerView.ClickonTagInterface
import java.util.ArrayList

class StickerTagAppAdapter(private val mcontext: Context, val clickonTagInterface : ClickonTagInterface, var mSearchTagList: ArrayList<TagList>?) : RecyclerView.Adapter<StickerTagAppAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_tag_stickers, null, true)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mSearchTagList!!.get(position)
        holder.tvTag.text = data.name
        holder.itemView.setOnClickListener {
            Log.i("TagId",data.id)
            clickonTagInterface.tagClick(data.id)
        }
    }

    override fun getItemCount(): Int {
        if(mSearchTagList != null){
            return mSearchTagList!!.size
        }else{
            return 0
        }

    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tvTag : TextView
        init {
            tvTag = itemView!!.findViewById(R.id.tv_category_tag) as TextView
        }
    }
}