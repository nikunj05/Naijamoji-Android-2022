package com.naijamojiapp.app.interfaces;


import com.naijamojiapp.app.response.EmojiListResponse;


public interface StickerClicked {
    public void mStickerClicked(EmojiListResponse.Result result,String shareOrNot);
}
