package com.whiner.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.whiner.network.base.CacheModel;
import com.whiner.network.base.GetService;
import com.whiner.network.base.RetrofitUtils;

import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public enum NetUtils {
    INSTANCE;

    private static final String TAG = "NetUtils";
    private final GetService getService;

    NetUtils() {
        getService = RetrofitUtils.INSTANCE.createStringService(GetService.class);
    }

    public interface OnListener<T> {

        TypeToken<T> getTypeToken();

        void onStart(Disposable d);

        void onSucceeded(T data, boolean isCache);

        void onFailed(Exception e);

        void onEnd();

    }

    public <T> void get(@NonNull String url, @NonNull CacheModel cacheModel, @NonNull OnListener<T> listener) {
        boolean actionListener = true;
        T data;
        switch (cacheModel.getType()) {
            case NO_CACHE:
                Log.d(TAG, "get: 不使用缓存，不操作" + url);
                break;
            case ONLY_CACHE:
                Log.d(TAG, "get: 使用缓存，先读取缓存，有缓存就不请求了" + url);
                data = readCache(cacheModel, listener.getTypeToken());
                if (data != null) {
                    listener.onStart(null);
                    listener.onSucceeded(data, true);
                    listener.onEnd();
                    return;
                }
                break;
            case FIRST_CACHE:
                Log.d(TAG, "get: 使用缓存，先读取缓存，再请求并更新缓存" + url);
                data = readCache(cacheModel, listener.getTypeToken());
                if (data != null) {
                    listener.onStart(null);
                    listener.onSucceeded(data, true);
                    listener.onEnd();
                    actionListener = false;
                }
                break;
            default:
                break;
        }
        final boolean b = actionListener;
        getService.get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (b) {
                            listener.onStart(d);
                        }
                    }


                    @Override
                    public void onNext(String s) {
                        try {
                            //保存数据
                            saveCache(cacheModel, s);
                            if (!b) {
                                return;
                            }
                            //使用数据
                            T data = parseData(listener.getTypeToken().getType(), s);
                            listener.onSucceeded(data, false);
                        } catch (Exception e) {
                            listener.onFailed(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (b) {
                            listener.onFailed(new Exception(e.getMessage()));
                            listener.onEnd();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (b) {
                            listener.onEnd();
                        }
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private <T> T parseData(@NonNull final Type type, final String s) {
        if (s == null) {
            return null;
        }
        T data = null;
        try {
            if (type == String.class) {
                data = (T) s;
            } else {
                data = GsonUtils.fromJson(s, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void saveCache(@NonNull CacheModel cacheModel, String s) {
        if (cacheModel.getType() == CacheModel.Type.NO_CACHE || s == null) {
            return;
        }
        NetMMKVUtils.INSTANCE.put(cacheModel.getKey(), cacheModel.getKeep_time(), s);
    }

    private <T> T readCache(@NonNull CacheModel cacheModel, @NonNull TypeToken<T> typeToken) {
        String s = NetMMKVUtils.INSTANCE.get(cacheModel.getKey());
        return parseData(typeToken.getType(), s);
    }

}
