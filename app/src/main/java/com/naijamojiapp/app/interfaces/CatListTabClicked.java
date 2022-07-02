package com.naijamojiapp.app.interfaces;


import com.naijamojiapp.app.response.EmojiListResponse;
import com.naijamojiapp.app.roomDB.entity.CatListTab;


public interface CatListTabClicked {
    void mTabClick(CatListTab resultm,int position,Boolean isClose,String isLogin,Boolean isPositionZero);
}
