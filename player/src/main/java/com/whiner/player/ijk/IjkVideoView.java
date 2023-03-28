package com.whiner.player.ijk;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.doikki.videoplayer.player.VideoView;

public class IjkVideoView extends VideoView {

    public IjkVideoView(@NonNull Context context) {
        super(context);
        init();
    }

    public IjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPlayerFactory(new DkIjkPlayerFactory());
        mCurrentScreenScaleType = SCREEN_SCALE_MATCH_PARENT;
    }

}
