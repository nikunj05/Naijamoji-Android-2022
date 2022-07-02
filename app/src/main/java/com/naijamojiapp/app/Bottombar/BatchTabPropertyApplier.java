package com.naijamojiapp.app.Bottombar;


class BatchTabPropertyApplier {
    private final BottomBar bottomBar;

    BatchTabPropertyApplier(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    void applyToAllTabs(TabPropertyUpdater propertyUpdater) {
        int tabCount = bottomBar.getTabCount();

        if (tabCount > 0) {
            int len = tabCount;
            for (int i = 0; i < len; i++) {
                BottomBarTab tab = bottomBar.getTabAtPosition(i);
                propertyUpdater.update(tab);
            }
        }
    }

    interface TabPropertyUpdater {
        void update(BottomBarTab tab);
    }
}
