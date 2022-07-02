package com.naijamojiapp.app.interfaces.newApp;


import com.naijamojiapp.app.roomDB.entity.CatListTab;


public interface CatListTabClickedInApp {
    void mTabClick(CatListTab resultm, int position, Boolean isClose, String isLogin, Boolean isPositionZero);
}
