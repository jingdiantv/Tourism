package com.whiner.tourism;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hjq.permissions.Permission;
import com.whiner.common.base.BaseActivity;
import com.whiner.network.NetUtils;
import com.whiner.network.base.CacheModel;
import com.whiner.network.base.NetResult;
import com.whiner.tourism.databinding.ActivityMainBinding;
import com.whiner.weather.OnWeatherListener;
import com.whiner.weather.WeatherBean;
import com.whiner.weather.tianqi.TianqiFactory;
import com.whiner.weather.tianqi.TianqiView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected boolean enableBg() {
        return true;
    }

    @Override
    protected boolean waitPermissionInit() {
        return true;
    }

    @Override
    protected List<String> requestPermissionList() {
        return Collections.singletonList(Permission.REQUEST_INSTALL_PACKAGES);
    }

    @Override
    protected boolean enableLoadingView() {
        return true;
    }

    @Override
    protected void init() {
        TianqiFactory.saveWeatherUrl("https://www.yiketianqi.com/free/day");
        getLifecycle().addObserver(binding.tianqi);
        //binding.vod.create(false);
        //binding.vod.setTitle("这里是标题");
        //binding.vod.play("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4", false);
        //binding.vod.play("http://hwrr.jx.chinamobile.com:8080/PLTV/88888888/224/3221225619/index.m3u8", false);

        CacheModel cacheModel = new CacheModel();
        cacheModel.setKey("hhh");
        cacheModel.setType(CacheModel.Type.FIRST_CACHE);
        cacheModel.setKeep_time(60 * 1000);
        NetUtils.INSTANCE.get("http:///api/settings/getSettings", cacheModel, new NetUtils.OnListener<NetResult<SettingsBean>>() {
            @Override
            public TypeToken<NetResult<SettingsBean>> getTypeToken() {
                return new TypeToken<NetResult<SettingsBean>>() {
                };
            }

            @Override
            public void onStart(Disposable d) {
                Log.d(TAG, "onStart: ");
            }

            @Override
            public void onSucceeded(NetResult<SettingsBean> data, boolean isCache) {
                Log.d(TAG, "onSucceeded: " + data + "\n" + isCache);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "onEnd: ");
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}