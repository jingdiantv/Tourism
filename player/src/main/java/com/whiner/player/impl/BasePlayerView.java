package com.whiner.player.impl;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.whiner.player.base.IPlayerView;

public abstract class BasePlayerView implements IPlayerView {

    protected String url;
    protected boolean mute = false;
    protected PlayState playState = PlayState.STATE_IDLE;
    protected OnPlayListener onPlayListener;
    protected final Handler handler = new Handler(Looper.myLooper());
    protected final Runnable f5ProgressRunnable = new Runnable() {
        @Override
        public void run() {
            f5Progress();
            handler.postDelayed(f5ProgressRunnable, 1000);
        }
    };

    @Override
    public void addToVideoView(ViewGroup root) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = getView(root.getContext());
        if (view != null) {
            root.addView(view, 0, layoutParams);
        }
        addListener();
    }

    @Override
    public void delToVideoView(ViewGroup root) {
        stopF5Progress();
        delListener();
        View view = getView(root.getContext());
        if (view != null) {
            root.removeView(view);
        }
        releaseView();
    }

    @Override
    public OnPlayListener getOnPlayListener() {
        return onPlayListener;
    }

    @Override
    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    @Override
    public PlayState getPlaySate() {
        return playState;
    }

    @Override
    public void setPlaySate(PlayState playState) {
        this.playState = playState;
        if (onPlayListener != null) {
            onPlayListener.onPlayState(playState);
        }
    }

    @Override
    public void setErrState(Exception e) {
        playState = PlayState.STATE_ERROR;
        if (onPlayListener != null) {
            onPlayListener.onPlayErr(e);
            onPlayListener.onPlayState(playState);
        }
    }

    @Override
    public void setCompletion() {
        playState = PlayState.STATE_PLAYBACK_COMPLETED;
        if (onPlayListener != null) {
            onPlayListener.onPlayCompleted();
            onPlayListener.onPlayState(playState);
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isMute() {
        return mute;
    }

    @Override
    public void setMute(boolean mute) {
        this.mute = mute;
        if (mute) {
            onMute();
            setPlaySate(PlayState.STATE_MUTE);
        } else {
            onUnMute();
            setPlaySate(PlayState.STATE_UN_MUTE);
        }
    }

    @Override
    public int getProgress() {
        if (isPlaying()) {
            long time1 = getCurrentPosition();
            long time2 = getDuration();
            if (time2 > 0) {
                float progress = time1 * 100f / time2;
                return (int) progress;
            }
        }
        return 0;
    }

    @Override
    public void setProgress(int progress) {
        float cur_time = progress / 100f * getDuration();
        seekTo((int) cur_time);
    }

    @Override
    public void startF5Progress() {
        handler.post(f5ProgressRunnable);
    }

    @Override
    public void stopF5Progress() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void f5Progress() {
        if (onPlayListener != null && isPlaying()) {
            long time1 = getCurrentPosition();
            long time2 = getDuration();
            //处理直播流的情况
            if (time2 <= 0) {
                time2 = 1;
                time1 = 1;
            }
            float progress = time1 * 100f / time2;
            onPlayListener.onProgress((int) progress, time1, time2);
        }
    }


}
