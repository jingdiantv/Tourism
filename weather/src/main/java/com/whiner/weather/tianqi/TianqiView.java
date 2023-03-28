package com.whiner.weather.tianqi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.whiner.weather.OnWeatherListener;
import com.whiner.weather.R;
import com.whiner.weather.WeatherBean;
import com.whiner.weather.databinding.LayoutTianqiViewBinding;

public class TianqiView extends ConstraintLayout implements LifecycleEventObserver, OnWeatherListener {

    public TianqiView(@NonNull Context context) {
        super(context);
        init();
    }

    public TianqiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TianqiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private LayoutTianqiViewBinding binding;
    private TianqiFactory tianqiFactory;

    private void init() {
        binding = LayoutTianqiViewBinding.inflate(LayoutInflater.from(getContext()), this, false);
        addView(binding.getRoot());
        tianqiFactory = new TianqiFactory();
    }

    private void setWeather(WeatherBean weather) {
        if (binding != null) {
            if (weather != null && weather.getCity() != null) {
                Glide.with(binding.ivWeatherWea).load(weather.getWea_img()).into(binding.ivWeatherWea);
                String tem = weather.getTem() + "°C";
                binding.tvWeatherTem.setText(tem);
                binding.tvWeatherWea.setText(weather.getWea());
                String wendu = weather.getTem_night() + "~" + weather.getTem_day();
                binding.tvWeatherWendu.setText(wendu);
                binding.tvWeatherCity.setText(weather.getCity());
                binding.tvWeatherErr.setVisibility(INVISIBLE);
            } else {
                Glide.with(binding.ivWeatherWea).load(R.drawable.ic_weather_err).into(binding.ivWeatherWea);
                binding.tvWeatherTem.setText("");
                binding.tvWeatherWea.setText("");
                binding.tvWeatherWendu.setText("");
                binding.tvWeatherCity.setText("");
                binding.tvWeatherErr.setText("暂无数据");
                binding.tvWeatherErr.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_RESUME:
                tianqiFactory.getData(TianqiView.this);
                break;
            case ON_PAUSE:
                tianqiFactory.stopGet();
                break;
            default:
                break;
        }
    }

    @Override
    public void onWeather(WeatherBean bean) {
        setWeather(bean);
    }

}
