package com.whiner.player.impl;

import androidx.annotation.Nullable;

import com.whiner.player.base.IPlayerView;
import com.whiner.player.base.IPlayerViewFactory;

public abstract class BasePlayerViewFactory<T extends IPlayerView> implements IPlayerViewFactory<T> {

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BasePlayerViewFactory) {
            if (getFactoryName() != null) {
                BasePlayerViewFactory<?> BasePlayerViewFactory = (BasePlayerViewFactory<?>) obj;
                return getFactoryName().equals(BasePlayerViewFactory.getFactoryName());
            }
        }
        return false;
    }

}
