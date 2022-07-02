package com.naijamojiapp.app.interfaces;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.naijamojiapp.app.roomDB.entity.RecentStickerList;


public interface RecentStickerClicked {
    void mRecentStickerClicked(Context context, int position, RecentStickerList data, String isSendOrNot, Bitmap bitmap, ImageView mImageVIew);
}
