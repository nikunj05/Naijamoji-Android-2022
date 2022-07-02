package com.naijamojiapp.ime.keyboard.KeyboardSearchView;
import android.util.Log;

import com.naijamojiapp.ime.keyboard.KeyboardSwitcher;
import com.naijamojiapp.ime.latin.common.StringUtils;


public class ExpressionsSearchHelper {
    public static void handleCode(boolean z, int i, int i2, KeyboardSwitcher keyboardSwitcher) {
        SearchView searchBar = keyboardSwitcher.getSearchBar();
        if (i == -5) {
            searchBar.handleDeleteKey();
            Log.i("callRestoreExpression", "=======>>>>"+"no1");
        } else if (i == -11) {
            Log.i("callRestoreExpression", "=======>>>>"+"yes");
            keyboardSwitcher.SearchClose();
            searchBar.clearFocus();
            searchBar.setCursorVisible(false);
        } else if (i2 == 10) {
            Log.i("callRestoreExpression", "=======>>>>"+"no2");
          //  keyboardSwitcher.performExpressionsSearch(searchBar.getText().toString());
//            searchBar.clearFocus();
//            searchBar.setCursorVisible(false);
        } else if (!z) {
            Log.i("callRestoreExpression", "=======>>>>"+"no3");
            if (searchBar.getAutoSpaceFlag() && !hasSpaceInEnd(keyboardSwitcher)) {
                insertTextIntoSearchBar(keyboardSwitcher, String.valueOf(' '));
            }
            searchBar.insertTextIntoSearchBar(String.valueOf((char) i2));
        }
    }

    public static void insertTextIntoSearchBar(KeyboardSwitcher keyboardSwitcher, String str) {
        keyboardSwitcher.getSearchBar().insertTextIntoSearchBar(str);
    }


    public static boolean hasSpaceInEnd(KeyboardSwitcher keyboardSwitcher) {
        return keyboardSwitcher.getSearchBar().hasSpaceInEnd();
    }

    public static boolean isBeginningOfEditor(KeyboardSwitcher keyboardSwitcher) {
        return StringUtils.isEmpty(keyboardSwitcher.getSearchBar().getTextInEditor());
    }
}
