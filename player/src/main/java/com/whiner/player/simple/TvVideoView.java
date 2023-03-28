package com.whiner.player.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

import com.whiner.common.view.TvSpinner;
import com.whiner.player.R;
import com.whiner.player.base.IPlayerControllerView;
import com.whiner.player.base.IPlayerView;
import com.whiner.player.base.IVideoView;

public class TvVideoView extends FrameLayout implements IVideoView, IPlayerView.OnPlayListener, IPlayerControllerView.OnControllerActionListener {

    private static final String TAG = "TvVideoView";

    private String mUrl;
    private TvSpinner mTvSpinner;
    private ProgressBar mProgressBar;
    private IPlayerView mPlayerView;
    private IPlayerControllerView mPlayerControllerView;

    public TvVideoView(@NonNull Context context) {
        super(context);
        init();
    }

    public TvVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        addLoadingView();
        setClickable(true);
        setFocusable(true);
        setOnClickListener(v -> {
            if (mPlayerControllerView != null) {
                mPlayerControllerView.toggleShow();
            }
        });
    }

    @Override
    public void addLoadingView() {
        mProgressBar = new ProgressBar(new ContextThemeWrapper(getContext(), R.style.ProgressBar), null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProgressBar.setId(R.id.pb_loading);
        mProgressBar.setVisibility(INVISIBLE);
        addView(mProgressBar, layoutParams);
    }

    @Override
    public void showLoadingView() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideLoadingView() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(INVISIBLE);
        }
    }

    @Override
    public IPlayerView getPlayerView() {
        return mPlayerView;
    }

    @Override
    public void setPlayerView(IPlayerView playerView) {
        if (mPlayerView != null) {
            mPlayerView.setOnPlayListener(null);
            mPlayerView.delToVideoView(this);
        }
        mPlayerView = playerView;
        if (mPlayerView != null) {
            mPlayerView.addToVideoView(this);
            mPlayerView.setOnPlayListener(this);
            //如果之前就有地址的话，继续播放
            if (getUrl() != null) {
                play(getUrl(), false);
            }
        }
        if (mPlayerControllerView != null) {
            mPlayerControllerView.hide();
        }
    }

    @Override
    public IPlayerControllerView getPlayerControllerView() {
        return mPlayerControllerView;
    }

    @Override
    public void setPlayerControllerView(IPlayerControllerView playerControllerView) {
        if (mPlayerControllerView != null) {
            mPlayerControllerView.setOnControllerActionListener(null);
            mPlayerControllerView.delToVideoView(this);
        }
        mPlayerControllerView = playerControllerView;
        if (mPlayerControllerView != null) {
            mPlayerControllerView.addToVideoView(this);
            mPlayerControllerView.setOnControllerActionListener(this);
        }
    }

    @Override
    public void create(boolean isLive) {
        if (isLive) {
            Log.d(TAG, "create: 直播模式无需控制器");
        } else {
            setPlayerControllerView(new TvPlayerControllerView());
        }
        readConfig();
    }

    @Override
    public void readConfig() {
        setPlayerView(PlayerConfig.INSTANCE.getCurPlayer().getPlayerView());
    }

    @Override
    public void setTitle(String title) {
        if (mPlayerControllerView != null) {
            mPlayerControllerView.setTitle(title);
        }
    }

    @Override
    public void play(String url, boolean looping) {
        mUrl = url;
        if (mPlayerView != null) {
            mPlayerView.setLooping(looping);
            mPlayerView.setUrl(url);
            mPlayerView.start();
        }
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void resume() {
        if (mPlayerView != null && !mPlayerView.isPlaying()) {
            mPlayerView.resume();
        }
    }

    @Override
    public void pause() {
        if (mPlayerView != null) {
            mPlayerView.pause();
        }
    }

    @Override
    public void destroy() {
        if (mTvSpinner != null) {
            mTvSpinner.destroy();
            mTvSpinner = null;
        }
        setPlayerView(null);
        setPlayerControllerView(null);
    }

    @Override
    public void onClickView(View view) {
        int id = view.getId();
        if (id == R.id.btn1) {
            if (mPlayerView != null) {
                if (mPlayerView.isPlaying()) {
                    mPlayerView.pause();
                } else {
                    mPlayerView.resume();
                }
            }
        } else if (id == R.id.btn2) {
            if (mPlayerView != null) {
                mPlayerView.replay();
            }
        } else if (id == R.id.btn3) {
            if (mPlayerView != null) {
                mPlayerView.setMute(!mPlayerView.isMute());
            }
        } else if (id == R.id.btn4) {
            if (mTvSpinner == null) {
                mTvSpinner = new TvSpinner(getContext(), "选择播放器内核", PlayerConfig.INSTANCE.getPlayerList(), PlayerConfig.INSTANCE.getCurPlayerIndex());
                mTvSpinner.setOnSelectItemListener((item, index) -> {
                    PlayerConfig.INSTANCE.select(index);
                    setPlayerView(PlayerConfig.INSTANCE.getCurPlayer().getPlayerView());
                });
            }
            mTvSpinner.show();
        } else {
            Log.d(TAG, "onClickView: 未知按钮");
        }
    }

    @Override
    public void onUserChangeProgress(int progress) {
        if (mPlayerView != null) {
            //强制进度调整不能超过95
            if (progress > 95) {
                progress = 95;
            }
            mPlayerView.setProgress(progress);
        }
    }

    @Override
    public void onShow() {
        Log.d(TAG, "onShow: 控制器显示了");
        if (mPlayerView != null) {
            mPlayerView.startF5Progress();
        }
    }

    @Override
    public void onHide() {
        Log.d(TAG, "onShow: 控制器隐藏了");
        if (mPlayerView != null) {
            mPlayerView.stopF5Progress();
        }
    }

    @Override
    public void onPlayErr(Exception e) {
        if (mPlayerControllerView != null) {
            mPlayerControllerView.setErrUI();
        }
    }

    @Override
    public void onPlayCompleted() {
        if (mPlayerControllerView != null) {
            mPlayerControllerView.setCompletedUI();
        }
    }

    @Override
    public void onPlayState(IPlayerView.PlayState state) {
        Log.d(TAG, "onPlayState: " + state);
        switch (state) {
            case STATE_PLAYING:
                //处理加载
                hideLoadingView();
                if (mPlayerControllerView != null) {
                    mPlayerControllerView.setPlayingUI();
                }
                break;
            case STATE_PAUSED:
                if (mPlayerControllerView != null) {
                    mPlayerControllerView.setPauseUI();
                }
                break;
            case STATE_MUTE:
                if (mPlayerControllerView != null) {
                    mPlayerControllerView.setMuteUI(true);
                }
                break;
            case STATE_UN_MUTE:
                if (mPlayerControllerView != null) {
                    mPlayerControllerView.setMuteUI(false);
                }
                break;
            case STATE_BUFFERING:
                showLoadingView();
                break;
            case STATE_BUFFERED:
                hideLoadingView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgress(int progress, long time1, long time2) {
        Log.d(TAG, "onProgress: " + progress + " - " + time1 + " - " + time2);
        if (mPlayerControllerView != null) {
            mPlayerControllerView.setProgress(time1, time2);
        }
    }

}
