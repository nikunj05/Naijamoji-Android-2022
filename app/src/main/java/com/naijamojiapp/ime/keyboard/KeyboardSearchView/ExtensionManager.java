package com.naijamojiapp.ime.keyboard.KeyboardSearchView;

import android.content.res.Configuration;
import android.view.inputmethod.EditorInfo;

import java.util.HashMap;

public interface ExtensionManager {
    void dismissCurrentExtension();

    void dismissOverlayExtension();

    boolean doesExtNeedKeyboard();

    boolean doesExtensionNeedCodeInput();

    void handleCodeInput(boolean z, int i, int i2);

    void hideDefaultExtension();

    void insertTextIntoEditor(String str);

    void onConfigurationChanged(Configuration configuration);

    void onNetworkConnected();

    void onStartInputView(EditorInfo editorInfo, boolean z, boolean z2, String str, boolean z3, boolean z4);

    void savePreviousExtensionState();

    void setHeightOfKeyboard(int i);

    void showDefaultExtension();

    void showMenuExtension(HashMap<String, String> hashMap);

    void showRequiredExtension();

    void updateDefaultExtension(boolean z, String str, boolean z2, boolean z3);

    void updateTransliterationState(boolean z);
}
