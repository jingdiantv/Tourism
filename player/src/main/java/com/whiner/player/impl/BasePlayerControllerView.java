package com.whiner.player.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.whiner.player.base.IPlayerControllerView;

public abstract class BasePlayerControllerView<V extends ViewBinding> implements IPlayerControllerView {

    protected V binding;
    protected boolean show = true;
    protected OnControllerActionListener onControllerActionListener;
    protected final long autoHideTime = 5000;
    protected final Handler handler = new Handler(Looper.myLooper());
    protected final Runnable hideRunnable = this::hide;

    protected abstract V getBinding(LayoutInflater layoutInflater);

    @Override
    public View getView(Context context) {
        if (binding == null) {
            binding = getBinding(LayoutInflater.from(context));
        }
        return binding.getRoot();
    }

    @Override
    public void addToVideoView(ViewGroup root) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = getView(root.getContext());
        int index = 1;
        if (view != null) {
            if (root.getChildCount() <= 1) {
                index = 0;
            }
            root.addView(view, index, layoutParams);
        }
        addListener();
    }

    @Override
    public void delToVideoView(ViewGroup root) {
        cancelAutoHide();
        delListener();
        View view = getView(root.getContext());
        if (view != null) {
            root.removeView(view);
        }
        releaseView();
    }

    @Override
    public OnControllerActionListener getOnControllerActionListener() {
        return onControllerActionListener;
    }

    @Override
    public void setOnControllerActionListener(OnControllerActionListener onControllerActionListener) {
        this.onControllerActionListener = onControllerActionListener;
    }

    @Override
    public void show() {
        autoHide();
        if (show) {
            return;
        }
        show = true;
        if (onControllerActionListener != null) {
            onControllerActionListener.onShow();
        }
        if (binding != null) {
            binding.getRoot().setVisibility(View.VISIBLE);
            showRequestFocus();
        }
    }

    @Override
    public void hide() {
        cancelAutoHide();
        if (!show) {
            return;
        }
        show = false;
        if (onControllerActionListener != null) {
            onControllerActionListener.onHide();
        }
        if (binding != null) {
            binding.getRoot().setVisibility(View.INVISIBLE);
            //强制焦点回到父
            if (binding.getRoot().getParent() instanceof View) {
                ((View) binding.getRoot().getParent()).requestFocus();
            }
        }
    }

    @Override
    public boolean isShow() {
        return show;
    }

    @Override
    public void toggleShow() {
        if (isShow()) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public void autoHide() {
        cancelAutoHide();
        handler.postDelayed(hideRunnable, autoHideTime);
    }

    @Override
    public void cancelAutoHide() {
        handler.removeCallbacksAndMessages(null);
    }

}
