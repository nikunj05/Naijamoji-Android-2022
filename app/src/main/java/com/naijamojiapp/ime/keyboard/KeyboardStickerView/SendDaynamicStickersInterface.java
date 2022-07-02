package com.naijamojiapp.ime.keyboard.KeyboardStickerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.naijamojiapp.app.response.newResponse.StickerListByTagOrCategory;
import com.naijamojiapp.app.roomDB.entity.AllStickerList;

public interface SendDaynamicStickersInterface {
    void sendDaynamicSticekrs(Context context, int position, StickerListByTagOrCategory.Result data, String isSendOrNot, Bitmap bitmap, ImageView mImageVIew);
}
