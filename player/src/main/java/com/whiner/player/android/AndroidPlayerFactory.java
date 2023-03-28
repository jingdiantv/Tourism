package com.whiner.player.android;

import com.whiner.player.impl.BasePlayerViewFactory;

public class AndroidPlayerFactory extends BasePlayerViewFactory<AndroidPlayer> {

    @Override
    public String getFactoryName() {
        return "原生播放器";
    }

    @Override
    public AndroidPlayer getPlayerView() {
        return new AndroidPlayer();
    }

}
