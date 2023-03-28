package com.whiner.player.ijk;

import android.content.Context;

import xyz.doikki.videoplayer.player.PlayerFactory;

public class DkIjkPlayerFactory extends PlayerFactory<DkIjkPlayer> {

    @Override
    public DkIjkPlayer createPlayer(Context context) {
        return new DkIjkPlayer(context);
    }

}
