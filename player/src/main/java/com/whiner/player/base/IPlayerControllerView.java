package com.whiner.player.base;

import android.view.View;

public interface IPlayerControllerView extends IView {

    OnControllerActionListener getOnControllerActionListener();

    void setOnControllerActionListener(OnControllerActionListener onControllerActionListener);

    void setTitle(String title);

    void setProgress(int progress);

    void setProgress(long time1, long time2);

    void setPlayingUI();

    void setPauseUI();

    void setErrUI();

    void setCompletedUI();

    void setMuteUI(boolean mute);

    void show();

    void hide();

    boolean isShow();

    void showRequestFocus();

    void toggleShow();

    void autoHide();

    void cancelAutoHide();

    interface OnControllerActionListener {

        void onClickView(View view);

        void onUserChangeProgress(int progress);

        void onShow();

        void onHide();

    }

}
