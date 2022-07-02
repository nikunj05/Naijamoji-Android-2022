package com.naijamojiapp.ime.keyboard.KeyboardStickerView

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
import java.util.ArrayList

class StickerTagKeyboardAdapter(private val mcontext: Context, val clickonTagInterface : ClickonTagInterface, var mSearchTagList: ArrayList<TagList>?) : RecyclerView.Adapter<StickerTagKeyboardAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_stickertag, null, true)
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
            tvTag = itemView!!.findViewById(R.id.tvTag) as TextView
        }
    }
}