package com.naijamojiapp.ime.keyboard.KeyboardStickerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.naijamojiapp.app.response.EmojiListResponse;
import com.naijamojiapp.app.roomDB.entity.AllStickerList;

import java.io.File;

public interface SendStickersInterface {
    void sendSticekrs(Context context, int position, AllStickerList data , String isSendOrNot, Bitmap bitmap,ImageView mImageVIew);
}
