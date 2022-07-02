package com.naijamojiapp.app.adapter.newadapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naijamojiapp.R
import com.naijamojiapp.app.interfaces.CatListTabClicked
import com.naijamojiapp.app.interfaces.newApp.CatListTabClickedInApp
import com.naijamojiapp.app.roomDB.entity.CatListTab
import com.naijamojiapp.app.utils.Preferences
import java.lang.Exception
import java.util.*

class CatTabInAppAdapter(val mcontext: Context, var mEmojiTab: ArrayList<CatListTab>?, var mCatListTabClicker : CatListTabClickedInApp) : RecyclerView.Adapter<CatTabInAppAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mcontext).inflate(R.layout.item_tab, null, true)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mEmojiTab!!.get(position)

        if(mEmojiTab!!.get(position).select){
            holder.ivTab.setColorFilter(mcontext.resources.getColor(R.color.dark_new))
        }else{
            holder.ivTab.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN)
        }

        Glide.with(mcontext).load(data.image).into(holder.ivTab)

        holder.itemView.setOnClickListener {
            if(!mEmojiTab!!.get(position).select){
                for (i in mEmojiTab!!.indices) {
                    if(mEmojiTab!!.get(i).id.equals(data.id)){
                        if(mEmojiTab!!.get(i).select){
                            //Already open so close stickers
                        }else{
                            //Not open stickers view
                            mEmojiTab!!.get(i).select = true
                            holder.ivTab.setColorFilter(mcontext.resources.getColor(R.color.dark_new))
                            notifyItemChanged(i)
                        }
                    }else{
                        mEmojiTab!!.get(i).select = false
                        holder.ivTab.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN)
                        notifyItemChanged(i)
                    }
                }
                try {
                    if(position == 0){
                        if(mEmojiTab!!.get(position).select){
                            mCatListTabClicker.mTabClick(data,position,mEmojiTab!!.get(position).select,"1",true)
                        }
                    }else{
                        if(mEmojiTab!!.get(position).select){
                            mCatListTabClicker.mTabClick(data,position,mEmojiTab!!.get(position).select,"1",false)
                        }
                    }
                }catch (e:Exception){}

            }
        }
    }


    fun firstPostionItemMakeSelected(){
        for (i in mEmojiTab!!.indices) {
            mEmojiTab!![i].select = i==0
            notifyItemChanged(i)
        }
    }

    override fun getItemCount(): Int {
        if(mEmojiTab != null){
            return mEmojiTab!!.size
        }else{
            return 0
        }
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var ivTab : ImageView
        init {
            ivTab = itemView!!.findViewById(R.id.ivTab) as ImageView
        }
    }
}