package com.whiner.common.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public enum MMKVUtils {
    INSTANCE;

    private static final String TAG = "MMKVUtils";

    public void init(final Context context) {
        String root = MMKV.initialize(context);
        Log.d(TAG, "initMMKV: MMKV root is " + root);
    }

    public String get(String key) {
        long time = MMKV.defaultMMKV().getLong(key + "_time", 0);
        if (time > System.currentTimeMillis()) {
            return MMKV.defaultMMKV().getString(key, null);
        } else {
            MMKV.defaultMMKV().removeValueForKey(key);
            MMKV.defaultMMKV().removeValueForKey(key + "_time");
        }
        return null;
    }

    public void put(String key, long keep_time, String s) {
        MMKV.defaultMMKV().putString(key, s);
        MMKV.defaultMMKV().putLong(key + "_time", System.currentTimeMillis() + keep_time);
    }

}
