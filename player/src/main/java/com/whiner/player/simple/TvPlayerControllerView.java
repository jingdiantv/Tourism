package com.whiner.player.simple;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.hjq.toast.Toaster;
import com.whiner.common.view.TvSeekBar;
import com.whiner.player.R;
import com.whiner.player.databinding.LayoutTvPlayerControllerViewBinding;
import com.whiner.player.impl.BasePlayerControllerView;

public class TvPlayerControllerView extends BasePlayerControllerView<LayoutTvPlayerControllerViewBinding> implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener {

    @Override
    public void setTitle(String title) {
        if (binding != null) {
            binding.tvTitle.setText(title);
        }
    }

    @Override
    public void setProgress(int progress) {
        if (binding != null) {
            binding.tpProgress.setProgress(progress);
        }
    }

    @Override
    public void setProgress(long time1, long time2) {
        if (binding != null) {
            binding.tpProgress.setProgress(time1, time2);
        }
    }

    @Override
    public void setPlayingUI() {
        if (binding != null) {
            binding.btn1.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void setPauseUI() {
        if (binding != null) {
            binding.btn1.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    public void setErrUI() {
        if (binding != null) {
            Toaster.show("播放出错");
            binding.btn1.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    public void setCompletedUI() {
        if (binding != null) {
            Toaster.show("播放结束");
            binding.btn1.setBackgroundResource(R.drawable.ic_play);
            binding.tpProgress.setProgress(0);
        }
    }

    @Override
    public void setMuteUI(boolean mute) {
        if (mute) {
            binding.btn3.setBackgroundResource(R.drawable.ic_voice);
        } else {
            binding.btn3.setBackgroundResource(R.drawable.ic_mute);
        }
    }

    @Override
    public void showRequestFocus() {
        if (binding != null) {
            binding.btn1.requestFocus();
        }
    }

    @Override
    public void addListener() {
        if (binding != null) {
            binding.tpProgress.setOnTvSeekBarListener(new TvSeekBar.OnTvSeekBarListener() {
                @Override
                public void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartPreview(TvSeekBar tvSeekBar, int progress) {
                    cancelAutoHide();
                }

                @Override
                public void onStopPreview(TvSeekBar tvSeekBar, int progress) {
                    autoHide();
                    if (onControllerActionListener != null) {
                        onControllerActionListener.onUserChangeProgress(progress);
                    }
                }

                @Override
                public void onBack() {
                    hide();
                }
            });
            binding.btn1.setOnFocusChangeListener(this);
            binding.btn2.setOnFocusChangeListener(this);
            binding.btn3.setOnFocusChangeListener(this);
            binding.btn4.setOnFocusChangeListener(this);
            binding.btn1.setOnClickListener(this);
            binding.btn2.setOnClickListener(this);
            binding.btn3.setOnClickListener(this);
            binding.btn4.setOnClickListener(this);
            binding.btn1.setOnKeyListener(this);
            binding.btn2.setOnKeyListener(this);
            binding.btn3.setOnKeyListener(this);
            binding.btn4.setOnKeyListener(this);
        }
    }

    @Override
    public void delListener() {
        if (binding != null) {
            binding.tpProgress.setOnTvSeekBarListener(null);
            binding.btn1.setOnFocusChangeListener(null);
            binding.btn2.setOnFocusChangeListener(null);
            binding.btn3.setOnFocusChangeListener(null);
            binding.btn4.setOnFocusChangeListener(null);
            binding.btn1.setOnClickListener(null);
            binding.btn2.setOnClickListener(null);
            binding.btn3.setOnClickListener(null);
            binding.btn4.setOnClickListener(null);
            binding.btn1.setOnKeyListener(null);
            binding.btn2.setOnKeyListener(null);
            binding.btn3.setOnKeyListener(null);
            binding.btn4.setOnKeyListener(null);
        }
    }

    @Override
    public void releaseView() {
        binding = null;
    }

    @Override
    protected LayoutTvPlayerControllerViewBinding getBinding(LayoutInflater layoutInflater) {
        return LayoutTvPlayerControllerViewBinding.inflate(layoutInflater);
    }

    @Override
    public void onClick(View v) {
        try {
            if (onControllerActionListener != null) {
                onControllerActionListener.onClickView(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            show();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            hide();
            return true;
        }
        return false;
    }

}
