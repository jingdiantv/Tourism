package com.whiner.player.simple;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.whiner.player.android.AndroidPlayerFactory;
import com.whiner.player.ijk.IjkPlayerFactory;
import com.whiner.player.impl.BasePlayerViewFactory;

import java.util.ArrayList;
import java.util.List;

public enum PlayerConfig {
    INSTANCE;

    private static final String TAG = "PlayerConfig";
    private final List<BasePlayerViewFactory<?>> list = new ArrayList<>();
    private BasePlayerViewFactory<?> mFactory = null;

    PlayerConfig() {
        list.add(new AndroidPlayerFactory());
        list.add(new IjkPlayerFactory());
    }

    public void init() {
        mFactory = (BasePlayerViewFactory<?>) CacheDiskUtils.getInstance().getSerializable(TAG, list.get(0));
    }

    public void add(BasePlayerViewFactory<?> factory) {
        list.add(factory);
    }

    public void select(int index) {
        if (index < list.size()) {
            mFactory = list.get(index);
            CacheDiskUtils.getInstance().put(TAG, mFactory);
        }
    }

    public BasePlayerViewFactory<?> getCurPlayer() {
        if (mFactory == null) {
            select(0);
        }
        return mFactory;
    }

    public int getCurPlayerIndex() {
        if (mFactory != null) {
            return list.indexOf(mFactory);
        }
        return 0;
    }

    public List<String> getPlayerList() {
        List<String> stringList = new ArrayList<>();
        for (BasePlayerViewFactory<?> item : list) {
            stringList.add(item.getFactoryName());
        }
        return stringList;
    }

}
