package com.whiner.player.ijk;

import com.whiner.player.impl.BasePlayerViewFactory;

public class IjkPlayerFactory extends BasePlayerViewFactory<IjkPlayer> {

    @Override
    public String getFactoryName() {
        return "IJK播放器";
    }

    @Override
    public IjkPlayer getPlayerView() {
        return new IjkPlayer();
    }

}
