package com.naijamojiapp.app.cusromsharedialog.Models;

import android.content.pm.ResolveInfo;

public class ShareActionModel {
    private String name;
    private String icon;
    private String type;
    private ResolveInfo appInfo;

    public ShareActionModel(ResolveInfo appInfo) {
        this.appInfo = appInfo;
    }

    public ShareActionModel(String name, String icon, String type) {
        this.name = name;
        this.icon = icon;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public ResolveInfo getAppInfo() {
        return appInfo;
    }
}
