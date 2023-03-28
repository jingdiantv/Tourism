package com.whiner.tourism;

import com.whiner.common.base.BaseApplication;
import com.whiner.player.simple.PlayerConfig;

public class App extends BaseApplication {

    @Override
    protected void init() {
        PlayerConfig.INSTANCE.init();
    }

}
