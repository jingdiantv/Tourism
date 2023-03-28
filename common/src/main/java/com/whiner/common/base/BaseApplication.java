package com.whiner.common.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.hjq.toast.Toaster;
import com.whiner.common.toaster.BigBlackToastStyle;
import com.whiner.common.utils.MMKVUtils;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        MMKVUtils.INSTANCE.init(this);
        initToaster();
        init();
    }

    protected void initToaster() {
        Toaster.init(this, new BigBlackToastStyle());
    }

    protected abstract void init();

}
