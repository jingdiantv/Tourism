package com.whiner.weather.tianqi;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.whiner.network.NetUtils;
import com.whiner.network.base.CacheModel;
import com.whiner.weather.OnWeatherListener;
import com.whiner.weather.R;
import com.whiner.weather.WeatherBean;
import com.whiner.weather.WeatherFactory;

import io.reactivex.disposables.Disposable;

public class TianqiFactory implements WeatherFactory {

    private static final String TAG = "TianqiFactory";
    private static final String defaultUrl = "https://www.yiketianqi.com/free/day";

    public static String getWeatherUrl() {
        return SPUtils.getInstance().getString(TAG, defaultUrl);
    }

    public static void saveWeatherUrl(String url) {
        SPUtils.getInstance().put(TAG, url);
    }

    private Disposable disposable;

    @Override
    public void getData(OnWeatherListener onWeatherListener) {
        CacheModel cacheModel = new CacheModel();
        cacheModel.setKey(TAG);
        cacheModel.setType(CacheModel.Type.ONLY_CACHE);
        cacheModel.setKeep_time(3600000);
        NetUtils.INSTANCE.get(getWeatherUrl(), cacheModel, new NetUtils.OnListener<TianqiBean>() {
            @Override
            public TypeToken<TianqiBean> getTypeToken() {
                return new TypeToken<TianqiBean>() {
                };
            }

            @Override
            public void onStart(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSucceeded(TianqiBean data, boolean isCache) {
                if (onWeatherListener != null) {
                    if (data != null) {
                        WeatherBean weatherBean = new WeatherBean();
                        weatherBean.setCity(data.getCity());
                        weatherBean.setWea(data.getWea());
                        weatherBean.setWea_img(getResIDByStr(data.getWea_img()));
                        weatherBean.setTem(data.getTem());
                        weatherBean.setTem_day(data.getTem_day());
                        weatherBean.setTem_night(data.getTem_night());
                        onWeatherListener.onWeather(weatherBean);
                    }
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (onWeatherListener != null) {
                    onWeatherListener.onWeather(null);
                }
            }

            @Override
            public void onEnd() {

            }
        });
    }

    @Override
    public void stopGet() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private static int getResIDByStr(String str) {
        if (str == null) {
            return R.drawable.ic_weather_err;
        }
        int icon;
        switch (str) {
            case "xue":
                icon = R.drawable.ic_weather_xue;
                break;
            case "lei":
                icon = R.drawable.ic_weather_lei;
                break;
            case "shachen":
                icon = R.drawable.ic_weather_shachen;
                break;
            case "wu":
                icon = R.drawable.ic_weather_wu;
                break;
            case "bingbao":
                icon = R.drawable.ic_weather_bingbao;
                break;
            case "yun":
                icon = R.drawable.ic_weather_yun;
                break;
            case "yu":
                icon = R.drawable.ic_weather_yu;
                break;
            case "yin":
                icon = R.drawable.ic_weather_yin;
                break;
            case "qing":
                icon = R.drawable.ic_weather_qing;
                break;
            default:
                icon = R.drawable.ic_weather_err;
                break;
        }
        return icon;
    }

}
