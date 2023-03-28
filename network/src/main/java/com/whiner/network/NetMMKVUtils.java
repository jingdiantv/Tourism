package com.whiner.network;

import com.tencent.mmkv.MMKV;

public enum NetMMKVUtils {
    INSTANCE;

    private static final String TAG = "NetMMKVUtils";

    private final MMKV mmkv;

    NetMMKVUtils() {
        mmkv = MMKV.mmkvWithID(TAG);
    }

    public String get(String key) {
        long time = mmkv.getLong(key + "_time", 0);
        if (time > System.currentTimeMillis()) {
            return mmkv.getString(key, null);
        } else {
            mmkv.removeValueForKey(key);
            mmkv.removeValueForKey(key + "_time");
        }
        return null;
    }

    public void put(String key, long keep_time, String s) {
        mmkv.putString(key, s);
        mmkv.putLong(key + "_time", System.currentTimeMillis() + keep_time);
    }

    public void clear() {
        mmkv.clearAll();
    }

}
