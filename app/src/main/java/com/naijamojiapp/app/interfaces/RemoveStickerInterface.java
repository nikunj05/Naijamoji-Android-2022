package com.naijamojiapp.app.interfaces;


import android.widget.ImageView;

import com.naijamojiapp.app.response.EmojiListResponse;
import com.naijamojiapp.app.response.newResponse.YourCustomStickersListResponse;


public interface RemoveStickerInterface {
    void mStickerlongPress(YourCustomStickersListResponse.Result result, int mPosition);
}
