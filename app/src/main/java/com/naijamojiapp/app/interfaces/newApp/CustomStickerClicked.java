package com.naijamojiapp.app.interfaces.newApp;


import com.naijamojiapp.app.response.newResponse.YourCustomStickersListResponse;


public interface CustomStickerClicked {
    public void mStickerClicked(YourCustomStickersListResponse.Result result, String shareOrNot);
}
