package com.naijamojiapp.app.interfaces.newApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.naijamojiapp.app.response.newResponse.StickerListByTagOrCategory;

public interface SendDaynamicStickersAppInterface {
    void sendDaynamicSticekrs(Context context, int position, StickerListByTagOrCategory.Result data, String isSendOrNot, Bitmap bitmap, ImageView mImageVIew);
}
