package com.naijamojiapp.app.interfaces.newApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.naijamojiapp.app.roomDB.entity.AllStickerList;

public interface SendLocalStickersAppInterface {
    void sendSticekrs(Context context, int position, AllStickerList data, String isSendOrNot, Bitmap bitmap, ImageView mImageVIew);
}
