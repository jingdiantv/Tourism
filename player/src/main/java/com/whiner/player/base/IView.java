package com.whiner.player.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface IView {

    View getView(Context context);

    void addToVideoView(ViewGroup root);

    void delToVideoView(ViewGroup root);

    void addListener();

    void delListener();

    void releaseView();

}
